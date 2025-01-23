import { Directive, Input, OnInit, TemplateRef, ViewContainerRef } from '@angular/core';
import { PermissionService } from '../services/permission.service';

@Directive({
  selector: '[hasPermission]'
})
export class HasPermissionDirective implements OnInit {
  private permission!: string;
  private isHidden = true;

  constructor(
    private templateRef: TemplateRef<any>,
    private viewContainer: ViewContainerRef,
    private permissionService: PermissionService
  ) {}

  @Input()
  set hasPermission(permission: string) {
    this.permission = permission;
    this.updateView();
  }

  ngOnInit() {
    this.updateView();
  }

  private updateView() {
    if (this.permission) {
      this.permissionService.hasPermission(this.permission).subscribe(
        hasPermission => {
          if (hasPermission && this.isHidden) {
            this.viewContainer.createEmbeddedView(this.templateRef);
            this.isHidden = false;
          } else if (!hasPermission && !this.isHidden) {
            this.viewContainer.clear();
            this.isHidden = true;
          }
        }
      );
    }
  }
}
