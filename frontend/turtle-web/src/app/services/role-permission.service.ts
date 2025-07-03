import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  RolePermission,
  RolePermissionCreateRequest,
  RolePermissionUpdateRequest,
  RolePermissionResponse,
  RolePermissionListResponse,
} from '../models/role-permission.model';
import { environment } from '../../environments/environment';
import { ApiResponse } from '../models/api.model';

@Injectable({
  providedIn: 'root',
})
export class RolePermissionService {
  private readonly baseUrl = `${environment.apiUrl}/role-permissions`;

  constructor(private http: HttpClient) {}

  /**
   * Get paginated list of role permissions
   */
  findAll(
    page = 0,
    size = 10,
    sort?: string
  ): Observable<RolePermissionListResponse> {
    let params = new HttpParams().set('page', page.toString()).set('size', size.toString());

    if (sort) {
      params = params.set('sort', sort);
    }

    return this.http.get<RolePermissionListResponse>(this.baseUrl, { params });
  }

  /**
   * Get role permission by ID
   */
  findById(id: number): Observable<RolePermissionResponse> {
    return this.http.get<RolePermissionResponse>(`${this.baseUrl}/${id}`);
  }

  /**
   * Create new role permission
   */
  create(data: RolePermissionCreateRequest): Observable<RolePermissionResponse> {
    return this.http.post<RolePermissionResponse>(this.baseUrl, data);
  }

  /**
   * Update role permission
   */
  update(id: number, data: RolePermissionUpdateRequest): Observable<RolePermissionResponse> {
    return this.http.put<RolePermissionResponse>(`${this.baseUrl}/${id}`, data);
  }

  /**
   * Delete role permission
   */
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  /**
   * Find role permissions by role name
   */
  findByRoleName(roleName: string): Observable<RolePermissionResponse> {
    return this.http.get<RolePermissionResponse>(`${this.baseUrl}/role/${roleName}`);
  }

  /**
   * Toggle active role permission
   */
  toggleActive(id: number): Observable<ApiResponse<RolePermission>> {
    return this.http.put<ApiResponse<RolePermission>>(`${this.baseUrl}/${id}/toggle-active`, {});
  }
}
