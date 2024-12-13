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

    return this.http.get<ApiResponse<Employee[]>>(`${this.API_URL}/unmapped`, {});
  }

  getActiveEmployees(): Observable<ApiResponse<Employee[]>> {

    return this.http.get<ApiResponse<Employee[]>>(`${this.API_URL}/active`, {});
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
    const params = this.getParams(page, size, sort);
    return this.http.get<ApiResponse<PageResponse<Employee>>>(this.API_URL, {params });
  }

  searchEmployees(query: string, page: number = 0, size: number = 10): Observable<ApiResponse<PageResponse<Employee>>> {
    const params = this.getParams(page, size)
      .set('query', query);
    return this.http.get<ApiResponse<PageResponse<Employee>>>(`${this.API_URL}/search`, { params });
  }

  getEmployeeById(id: number): Observable<ApiResponse<Employee>> {
    return this.http.get<ApiResponse<Employee>>(`${this.API_URL}/${id}`, {});
  }

  createEmployee(employee: Partial<Employee>): Observable<ApiResponse<Employee>> {
    return this.http.post<ApiResponse<Employee>>(this.API_URL, employee, {});
  }

  updateEmployee(id: number, employee: Partial<Employee>): Observable<ApiResponse<Employee>> {
    return this.http.put<ApiResponse<Employee>>(`${this.API_URL}/${id}`, employee, {});
  }

  deleteEmployee(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`, {});
  }
}
