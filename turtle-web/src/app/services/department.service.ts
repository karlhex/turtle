import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse, PageResponse } from '../models/api.model';

export interface Department {
  id?: number;
  name: string;         // 部门名称
  description?: string; // 部门描述
  code: string;         // 部门编码
  isActive: boolean;    // 是否激活
  parentId?: number;    // 父部门ID
  managerId?: number;   // 部门管理人员
}

@Injectable({
  providedIn: 'root'
})
export class DepartmentService {
  private readonly API_URL = 'http://localhost:8080/api/departments';

  constructor(private http: HttpClient) {}

  getDepartments(page: number = 0, size: number = 10): Observable<ApiResponse<PageResponse<Department>>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<ApiResponse<PageResponse<Department>>>(this.API_URL, { params });
  }

  searchDepartments(query: string, page: number = 0, size: number = 10): Observable<ApiResponse<PageResponse<Department>>> {
    const params = new HttpParams()
      .set('query', query)
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<ApiResponse<PageResponse<Department>>>(`${this.API_URL}/search`, { params });
  }

  getDepartmentById(id: number): Observable<ApiResponse<Department>> {
    return this.http.get<ApiResponse<Department>>(`${this.API_URL}/${id}`);
  }

  createDepartment(department: Department): Observable<ApiResponse<Department>> {
    return this.http.post<ApiResponse<Department>>(this.API_URL, department);
  }

  updateDepartment(id: number, department: Department): Observable<ApiResponse<Department>> {
    return this.http.put<ApiResponse<Department>>(`${this.API_URL}/${id}`, department);
  }

  deleteDepartment(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.API_URL}/${id}`);
  }
}
