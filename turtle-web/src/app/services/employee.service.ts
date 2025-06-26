import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse, PageResponse } from '../models/api.model';
import { AuthService } from './auth.service';
import { environment } from '../../environments/environment';
import {
  Employee,
  EmployeeEducation,
  EmployeeAttendance,
  EmployeeLeave,
  EmployeeRole,
} from '../models/employee.model';

@Injectable({
  providedIn: 'root',
})
export class EmployeeService {
  private readonly API_URL = `${environment.apiUrl}/employees`;

  constructor(private http: HttpClient, private authService: AuthService) {}

  getUnmappedEmployees(): Observable<ApiResponse<Employee[]>> {
    const headers = this.getHeaders();
    return this.http.get<ApiResponse<Employee[]>>(`${this.API_URL}/unmapped`, { headers });
  }

  getActiveEmployees(): Observable<ApiResponse<Employee[]>> {
    const headers = this.getHeaders();
    return this.http.get<ApiResponse<Employee[]>>(`${this.API_URL}/active`, { headers });
  }

  getEmployeesByRole(role: EmployeeRole): Observable<ApiResponse<Employee[]>> {
    const headers = this.getHeaders();
    return this.http.get<ApiResponse<Employee[]>>(`${this.API_URL}/by-role/${role}`, { headers });
  }

  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  private getParams(
    page: number,
    size: number,
    sort?: { sortBy?: string; direction?: 'ASC' | 'DESC' }
  ): HttpParams {
    let params = new HttpParams().set('page', page.toString()).set('size', size.toString());

    if (sort?.sortBy) {
      params = params.set('sortBy', sort.sortBy).set('direction', sort.direction || 'ASC');
    }

    return params;
  }

  getEmployees(
    page = 0,
    size = 10,
    sort?: { sortBy?: string; direction?: 'ASC' | 'DESC' }
  ): Observable<ApiResponse<PageResponse<Employee>>> {
    const headers = this.getHeaders();
    const params = this.getParams(page, size, sort);
    return this.http.get<ApiResponse<PageResponse<Employee>>>(this.API_URL, { headers, params });
  }

  search(
    query: string,
    page = 0,
    size = 10,
    sort?: { sortBy?: string; direction?: 'ASC' | 'DESC' }
  ): Observable<ApiResponse<PageResponse<Employee>>> {
    const headers = this.getHeaders();
    const params = this.getParams(page, size, sort).set('query', query);
    return this.http.get<ApiResponse<PageResponse<Employee>>>(`${this.API_URL}/search`, {
      headers,
      params,
    });
  }

  searchByRole(
    role: EmployeeRole,
    query: string,
    page = 0,
    size = 10
  ): Observable<ApiResponse<PageResponse<Employee>>> {
    const headers = this.getHeaders();
    const params = this.getParams(page, size).set('query', query).set('role', role);
    return this.http.get<ApiResponse<PageResponse<Employee>>>(`${this.API_URL}/search-by-role`, {
      headers,
      params,
    });
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

  updateEmployeeRole(id: number, role: EmployeeRole): Observable<ApiResponse<Employee>> {
    const headers = this.getHeaders();
    return this.http.put<ApiResponse<Employee>>(
      `${this.API_URL}/${id}/role`,
      { role },
      { headers }
    );
  }

  deleteEmployee(id: number): Observable<void> {
    const headers = this.getHeaders();
    return this.http.delete<void>(`${this.API_URL}/${id}`, { headers });
  }

  // Education related methods
  addEducation(
    employeeId: number,
    education: EmployeeEducation
  ): Observable<ApiResponse<EmployeeEducation>> {
    const headers = this.getHeaders();
    return this.http.post<ApiResponse<EmployeeEducation>>(
      `${this.API_URL}/${employeeId}/educations`,
      education,
      { headers }
    );
  }

  updateEducation(
    employeeId: number,
    educationId: number,
    education: EmployeeEducation
  ): Observable<ApiResponse<EmployeeEducation>> {
    const headers = this.getHeaders();
    return this.http.put<ApiResponse<EmployeeEducation>>(
      `${this.API_URL}/${employeeId}/educations/${educationId}`,
      education,
      { headers }
    );
  }

  deleteEducation(employeeId: number, educationId: number): Observable<void> {
    const headers = this.getHeaders();
    return this.http.delete<void>(`${this.API_URL}/${employeeId}/educations/${educationId}`, {
      headers,
    });
  }

  // Attendance related methods
  addAttendance(
    employeeId: number,
    attendance: EmployeeAttendance
  ): Observable<ApiResponse<EmployeeAttendance>> {
    const headers = this.getHeaders();
    return this.http.post<ApiResponse<EmployeeAttendance>>(
      `${this.API_URL}/${employeeId}/attendances`,
      attendance,
      { headers }
    );
  }

  updateAttendance(
    employeeId: number,
    attendanceId: number,
    attendance: EmployeeAttendance
  ): Observable<ApiResponse<EmployeeAttendance>> {
    const headers = this.getHeaders();
    return this.http.put<ApiResponse<EmployeeAttendance>>(
      `${this.API_URL}/${employeeId}/attendances/${attendanceId}`,
      attendance,
      { headers }
    );
  }

  deleteAttendance(employeeId: number, attendanceId: number): Observable<void> {
    const headers = this.getHeaders();
    return this.http.delete<void>(`${this.API_URL}/${employeeId}/attendances/${attendanceId}`, {
      headers,
    });
  }

  // Leave related methods
  addLeave(employeeId: number, leave: EmployeeLeave): Observable<ApiResponse<EmployeeLeave>> {
    const headers = this.getHeaders();
    return this.http.post<ApiResponse<EmployeeLeave>>(
      `${this.API_URL}/${employeeId}/leaves`,
      leave,
      { headers }
    );
  }

  updateLeave(
    employeeId: number,
    leaveId: number,
    leave: EmployeeLeave
  ): Observable<ApiResponse<EmployeeLeave>> {
    const headers = this.getHeaders();
    return this.http.put<ApiResponse<EmployeeLeave>>(
      `${this.API_URL}/${employeeId}/leaves/${leaveId}`,
      leave,
      { headers }
    );
  }

  deleteLeave(employeeId: number, leaveId: number): Observable<void> {
    const headers = this.getHeaders();
    return this.http.delete<void>(`${this.API_URL}/${employeeId}/leaves/${leaveId}`, { headers });
  }
}
