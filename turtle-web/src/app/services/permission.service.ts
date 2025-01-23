import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { ApiResponse } from '../models/api.model';

@Injectable({
  providedIn: 'root'
})
export class PermissionService {
  private readonly API_URL = `${environment.apiUrl}/permissions`;
  private userPermissions: Set<string> = new Set();
  private initialized = false;

  constructor(private http: HttpClient) {}

  initialize(): Observable<void> {
    if (this.initialized) {
      return of(void 0);
    }

    return this.http.get<ApiResponse<string[]>>(`${this.API_URL}/current`).pipe(
      map(response => {
        console.log('Server response:', response);
        if (response.code === 200 && Array.isArray(response.data)) {
          this.userPermissions = new Set(response.data);
        }
        this.initialized = true;
      }),
      catchError(() => {
        this.initialized = true;
        return of(void 0);
      })
    );
  }

  private matchPermission(required: string, granted: string): boolean {
    // Convert glob pattern to regex pattern
    const pattern = granted
      .replace(/\./g, '\\.')  // Escape dots
      .replace(/\*/g, '.*')   // Convert * to .*
      .replace(/\?/g, '.');   // Convert ? to .
    const regex = new RegExp(`^${pattern}$`);

    console.log('Checking permission:', required, ' against ', granted, ' ->', regex.test(required));
    return regex.test(required);
  }

  private checkPermission(permission: string): boolean {
    // Direct match
    if (this.userPermissions.has(permission)) {
      return true;
    }
    // Pattern match
    return Array.from(this.userPermissions).some(p => this.matchPermission(permission, p));
  }

  hasPermission(permission: string): Observable<boolean> {
    if (!this.initialized) {
      return this.initialize().pipe(
        map(() => this.checkPermission(permission))
      );
    }
    return of(this.checkPermission(permission));
  }

  hasAnyPermission(permissions: string[]): Observable<boolean> {
    if (!this.initialized) {
      return this.initialize().pipe(
        map(() => permissions.some(p => this.checkPermission(p)))
      );
    }
    return of(permissions.some(p => this.checkPermission(p)));
  }

  clearPermissions() {
    this.userPermissions.clear();
    this.initialized = false;
  }
}
