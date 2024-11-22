import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse, PageResponse } from '../core/models/api.model';
import { AuthService } from './auth.service';

export interface Employee {
  id?: number;
  name: string;
  employeeNumber: string;
  email: string;
  phone?: string;
  department: {
    id: number;
    name: string;
  };
  position: string;
  isActive: boolean;
  hireDate: string;
  leaveDate?: string;
  remarks?: string;
  emergencyContact?: {
    id?: number;
    firstName: string;
    lastName: string;
    address: string;
    email: string;
    phone: string;
  };
  birthday?: string;
  gender?: string;
  ethnicity?: string;
  contractType?: string;
  contractDuration?: number;
  contractStartDate?: string;
  idType?: string;
  idNumber: string;
}

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {
  private readonly API_URL = 'http://localhost:8080/api/employees';

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) { }

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

  deleteEmployee(id: number): Observable<ApiResponse<void>> {
    const headers = this.getHeaders();
    return this.http.delete<ApiResponse<void>>(`${this.API_URL}/${id}`, { headers });
  }
}
