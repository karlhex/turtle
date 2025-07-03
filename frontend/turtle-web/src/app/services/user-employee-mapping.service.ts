import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ApiResponse } from '../models/api.model';

export interface UserEmployeeMapping {
  id: number;
  userId: number;
  username: string;
  employeeId: number;
  employeeName: string;
}

@Injectable({
  providedIn: 'root',
})
export class UserEmployeeMappingService {
  private apiUrl = `${environment.apiUrl}/user-employee-mappings`;

  constructor(private http: HttpClient) {}

  getMappings(): Observable<ApiResponse<UserEmployeeMapping[]>> {
    return this.http.get<ApiResponse<UserEmployeeMapping[]>>(this.apiUrl);
  }

  createMapping(userId: number, employeeId: number): Observable<ApiResponse<void>> {
    return this.http.post<ApiResponse<void>>(this.apiUrl, null, {
      params: {
        userId: userId.toString(),
        employeeId: employeeId.toString(),
      },
    });
  }

  deleteMapping(mappingId: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/${mappingId}`);
  }

  getUserMapping(userId: number): Observable<ApiResponse<UserEmployeeMapping>> {
    return this.http.get<ApiResponse<UserEmployeeMapping>>(`${this.apiUrl}/user/${userId}`);
  }
}
