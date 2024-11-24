import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse, PageResponse } from '../core/models/api.model';
import { environment } from '../../environments/environment';

export interface EmployeeJobHistory {
  id?: number;
  employeeId: number;
  companyName: string;
  position: string;
  startDate: string;
  endDate?: string;
  department: string;
  jobDescription?: string;
  achievements?: string;
  leavingReason?: string;
  referenceContact?: string;
  remarks?: string;
}

@Injectable({
  providedIn: 'root'
})
export class EmployeeJobHistoryService {
  private apiUrl = `${environment.apiUrl}/employees`;

  constructor(private http: HttpClient) {}

  getJobHistory(employeeId: number): Observable<ApiResponse<EmployeeJobHistory[]>> {
    return this.http.get<ApiResponse<EmployeeJobHistory[]>>(`${this.apiUrl}/${employeeId}/job-history`);
  }

  createJobHistory(employeeId: number, jobHistory: EmployeeJobHistory): Observable<ApiResponse<EmployeeJobHistory>> {
    return this.http.post<ApiResponse<EmployeeJobHistory>>(`${this.apiUrl}/${employeeId}/job-history`, jobHistory);
  }

  updateJobHistory(employeeId: number, jobHistoryId: number, jobHistory: EmployeeJobHistory): Observable<ApiResponse<EmployeeJobHistory>> {
    return this.http.put<ApiResponse<EmployeeJobHistory>>(`${this.apiUrl}/${employeeId}/job-history/${jobHistoryId}`, jobHistory);
  }

  deleteJobHistory(employeeId: number, jobHistoryId: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/${employeeId}/job-history/${jobHistoryId}`);
  }
}