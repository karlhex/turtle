import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ApiResponse } from '../core/models/api.model';

export interface UserEmployeeMapping {
  id: number;
  userId: number;
  employeeId: number;
  username: string;
  userEmail: string;
  employeeName: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserEmployeeMappingService {
  private apiUrl = `${environment.apiUrl}/user-employee-mappings`;

  constructor(private http: HttpClient) {}

  getMappings(): Observable<ApiResponse<UserEmployeeMapping[]>> {
    return this.http.get<ApiResponse<UserEmployeeMapping[]>>(this.apiUrl);
  }

  getUserMappings(userId: number): Observable<ApiResponse<UserEmployeeMapping[]>> {
    return this.http.get<ApiResponse<UserEmployeeMapping[]>>(`${this.apiUrl}/user/${userId}`);
  }

  createMapping(userId: number, employeeId: number): Observable<ApiResponse<void>> {
    return this.http.post<ApiResponse<void>>(this.apiUrl, { userId, employeeId });
  }

  deleteMapping(mappingId: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/${mappingId}`);
  }
}
