import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class PermissionService {
  private readonly PERMISSIONS_KEY = 'user_permissions';
  private permissionsSubject = new BehaviorSubject<Set<string>>(this.loadPermissions());

  constructor() {
    // Initialize permission service
  }

  setPermissions(permissions: string[]): void {
    const permissionSet = new Set(permissions);
    localStorage.setItem(this.PERMISSIONS_KEY, JSON.stringify(Array.from(permissionSet)));
    this.permissionsSubject.next(permissionSet);
  }

  private loadPermissions(): Set<string> {
    const storedPermissions = localStorage.getItem(this.PERMISSIONS_KEY);
    return storedPermissions ? new Set(JSON.parse(storedPermissions)) : new Set();
  }

  hasPermission(permission: string): Observable<boolean> {
    return this.permissionsSubject.pipe(
      map(permissions => {
        if (!permission) {
          return true;
        }
        return Array.from(permissions).some(p => {
          const pattern = p.replace(/\./g, '\\.').replace(/\*/g, '.*');
          const regex = new RegExp(`^${pattern}$`);
          return regex.test(permission);
        });
      })
    );
  }

  hasAnyPermission(permissions: string[]): Observable<boolean> {
    return this.permissionsSubject.pipe(
      map(permSet => {
        const permPatterns = Array.from(permSet).map(p => {
          const pattern = p.replace(/\./g, '\\.').replace(/\*/g, '.*');
          return new RegExp(`^${pattern}$`);
        });
        return permissions.some(permission => permPatterns.some(regex => regex.test(permission)));
      })
    );
  }

  hasAllPermissions(permissions: string[]): Observable<boolean> {
    return this.permissionsSubject.pipe(
      map(permSet => {
        const permPatterns = Array.from(permSet).map(p => {
          const pattern = p.replace(/\./g, '\\.').replace(/\*/g, '.*');
          return new RegExp(`^${pattern}$`);
        });
        return permissions.every(permission => permPatterns.some(regex => regex.test(permission)));
      })
    );
  }

  clearPermissions(): void {
    localStorage.removeItem(this.PERMISSIONS_KEY);
    this.permissionsSubject.next(new Set());
  }

  getPermissions(): Set<string> {
    return new Set(this.permissionsSubject.value);
  }

  getPermissionsObservable(): Observable<Set<string>> {
    return this.permissionsSubject.asObservable();
  }

  // Helper method to check if user has any of the specified permissions
  hasAnyOfPermissions(permissions: string[]): Observable<boolean> {
    if (!permissions || permissions.length === 0) {
      return of(true);
    }
    return this.hasAnyPermission(permissions);
  }

  // Helper method to check role-based permissions
  checkRoleBasedAccess(requiredRoles: string[], currentRole: string): boolean {
    if (!requiredRoles || requiredRoles.length === 0) {
      return true;
    }
    return requiredRoles.includes(currentRole);
  }

  // Enhanced permission check with role fallback
  hasPermissionWithRoleFallback(permission: string, allowedRoles: string[], currentRole: string): Observable<boolean> {
    // If user role is in allowed roles, grant access regardless of specific permissions
    if (this.checkRoleBasedAccess(allowedRoles, currentRole)) {
      return of(true);
    }
    
    // Otherwise check specific permission
    return this.hasPermission(permission);
  }
}
