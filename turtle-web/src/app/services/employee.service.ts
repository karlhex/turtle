import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse, PageResponse } from '../models/api.model';
import { AuthService } from './auth.service';
import { environment } from '../../environments/environment';
import { Employee, EmployeeEducation, EmployeeAttendance, EmployeeLeave } from '../models/employee.model';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {
  private readonly API_URL = `${environment.apiUrl}/employees`;

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) { }

  getUnmappedEmployees(): Observable<ApiResponse<Employee[]>> {
    const headers = this.getHeaders();
    return this.http.get<ApiResponse<Employee[]>>(`${this.API_URL}/unmapped`, { headers });
  }

  getActiveEmployees(): Observable<ApiResponse<Employee[]>> {
    const headers = this.getHeaders();
    return this.http.get<ApiResponse<Employee[]>>(`${this.API_URL}/active`, { headers });
  }

  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  private getParams(page: number, size: number, sort?: { sortBy?: string; direction?: 'ASC' | 'DESC'; }): HttpParams {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    if (sort?.sortBy) {
      params = params
        .set('sortBy', sort.sortBy)
        .set('direction', sort.direction || 'ASC');
    }
    
    return params;
  }

  getEmployees(page: number = 0, size: number = 10, sort?: { sortBy?: string; direction?: 'ASC' | 'DESC'; }): Observable<ApiResponse<PageResponse<Employee>>> {
    const headers = this.getHeaders();
    const params = this.getParams(page, size, sort);
    return this.http.get<ApiResponse<PageResponse<Employee>>>(this.API_URL, { headers, params });
  }

  searchEmployees(query: string, page: number = 0, size: number = 10): Observable<ApiResponse<PageResponse<Employee>>> {
    const headers = this.getHeaders();
    const params = this.getParams(page, size)
      .set('query', query);
    return this.http.get<ApiResponse<PageResponse<Employee>>>(`${this.API_URL}/search`, { headers, params });
  }

  getEmployeeById(id: number): Observable<ApiResponse<Employee>> {
    const headers = this.getHeaders();
    return this.http.get<ApiResponse<Employee>>(`${this.API_URL}/${id}`, { headers });
  }

  createEmployee(employee: Partial<Employee>): Observable<ApiResponse<Employee>> {
    const headers = this.getHeaders();
    return this.http.post<ApiResponse<Employee>>(this.API_URL, employee, { headers });
  }

  updateEmployee(id: number, employee: Partial<Employee>): Observable<ApiResponse<Employee>> {
    const headers = this.getHeaders();
    return this.http.put<ApiResponse<Employee>>(`${this.API_URL}/${id}`, employee, { headers });
  }

  deleteEmployee(id: number): Observable<void> {
    const headers = this.getHeaders();
    return this.http.delete<void>(`${this.API_URL}/${id}`, { headers });
  }
}
