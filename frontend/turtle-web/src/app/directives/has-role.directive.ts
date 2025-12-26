import { Directive, Input, TemplateRef, ViewContainerRef, OnInit, OnDestroy } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Directive({
  selector: '[appHasRole]'
})
export class HasRoleDirective implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  private hasView = false;

  @Input() set appHasRole(roles: string | string[]) {
    this.roles = Array.isArray(roles) ? roles : [roles];
    this.updateView();
  }

  @Input() set appHasRoleHide(roles: string | string[]) {
    this.hideRoles = Array.isArray(roles) ? roles : [roles];
    this.updateView();
  }

  private roles: string[] = [];
  private hideRoles: string[] = [];

  constructor(
    private templateRef: TemplateRef<unknown>,
    private viewContainer: ViewContainerRef,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.authService.getUserRole()
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => {
        this.updateView();
      });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private updateView() {
    this.authService.getUserRole()
      .pipe(takeUntil(this.destroy$))
      .subscribe(userRole => {
        const shouldShow = this.checkRolePermission(userRole);
        
        if (shouldShow && !this.hasView) {
          this.viewContainer.createEmbeddedView(this.templateRef);
          this.hasView = true;
        } else if (!shouldShow && this.hasView) {
          this.viewContainer.clear();
          this.hasView = false;
        }
      });
  }

  private checkRolePermission(userRole: string): boolean {
    // If hideRoles is specified, check if current role should be hidden
    if (this.hideRoles.length > 0) {
      if (this.hideRoles.includes(userRole)) {
        return false;
      }
    }

    // If roles is specified, check if current role is allowed
    if (this.roles.length > 0) {
      return this.roles.includes(userRole);
    }

    // Default to show if no specific role restrictions
    return true;
  }
}