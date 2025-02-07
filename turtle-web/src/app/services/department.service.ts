import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse, PageResponse } from '../models/api.model';
import { environment } from 'src/environments/environment';
import { Department } from '../models/department.model';

@Injectable({
  providedIn: 'root'
})
export class DepartmentService {
  private readonly API_URL = `${environment.apiUrl}/departments`;

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
