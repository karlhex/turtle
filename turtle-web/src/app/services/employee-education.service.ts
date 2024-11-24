import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse } from '../core/models/api.model';
import { environment } from '../../environments/environment';

export interface EmployeeEducation {
  id?: number;
  employeeId: number;
  school: string;
  major: string;
  degree: string;
  startDate: string;
  endDate: string;
  certificateNumber: string;
  isHighestDegree: boolean;
  remarks: string;
}

@Injectable({
  providedIn: 'root'
})
export class EmployeeEducationService {
  private apiUrl = `${environment.apiUrl}/employees`;

  constructor(private http: HttpClient) {}

  getEducations(employeeId: number): Observable<ApiResponse<EmployeeEducation[]>> {
    return this.http.get<ApiResponse<EmployeeEducation[]>>(`${this.apiUrl}/${employeeId}/educations`);
  }

  addEducation(employeeId: number, education: EmployeeEducation): Observable<ApiResponse<EmployeeEducation>> {
    return this.http.post<ApiResponse<EmployeeEducation>>(`${this.apiUrl}/${employeeId}/educations`, education);
  }

  updateEducation(employeeId: number, educationId: number, education: EmployeeEducation): Observable<ApiResponse<EmployeeEducation>> {
    return this.http.put<ApiResponse<EmployeeEducation>>(`${this.apiUrl}/${employeeId}/educations/${educationId}`, education);
  }

  deleteEducation(employeeId: number, educationId: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/${employeeId}/educations/${educationId}`);
  }
}
