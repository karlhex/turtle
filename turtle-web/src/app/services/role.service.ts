import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Role, CreateRoleRequest, UpdateRoleRequest } from '../models/role.model';
import { ApiResponse } from '../models/api.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RoleService {
  private apiUrl = `${environment.apiUrl}/roles`;

  constructor(private http: HttpClient) {}

  /**
   * Get all roles
   */
  getAllRoles(): Observable<ApiResponse<Role[]>> {
    return this.http.get<ApiResponse<Role[]>>(this.apiUrl);
  }

  /**
   * Get system roles only
   */
  getSystemRoles(): Observable<ApiResponse<Role[]>> {
    return this.http.get<ApiResponse<Role[]>>(`${this.apiUrl}/system`);
  }

  /**
   * Get role by name
   */
  getRoleByName(name: string): Observable<ApiResponse<Role>> {
    return this.http.get<ApiResponse<Role>>(`${this.apiUrl}/${encodeURIComponent(name)}`);
  }

  /**
   * Create a new role
   */
  createRole(role: CreateRoleRequest): Observable<ApiResponse<Role>> {
    return this.http.post<ApiResponse<Role>>(this.apiUrl, role);
  }

  /**
   * Update an existing role
   */
  updateRole(id: number, role: UpdateRoleRequest): Observable<ApiResponse<Role>> {
    return this.http.put<ApiResponse<Role>>(`${this.apiUrl}/${id}`, role);
  }

  /**
   * Delete a role
   */
  deleteRole(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/${id}`);
  }

  /**
   * Get role display name (removes ROLE_ prefix)
   */
  getRoleDisplayName(role: Role): string {
    return role.name.replace('ROLE_', '');
  }

  /**
   * Format role name (adds ROLE_ prefix if not present)
   */
  formatRoleName(name: string): string {
    return name.startsWith('ROLE_') ? name : `ROLE_${name}`;
  }
}
