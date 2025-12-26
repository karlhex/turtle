import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ApiResponse } from '../models/api-response.model';

export interface ApprovalRequest {
  id: number;
  applicationId: number;
  applicantName: string;
  applicantEmail: string;
  requestType: string;
  status: string;
  submitTime: Date;
  currentStep: string;
  assignee?: string;
  priority: 'LOW' | 'MEDIUM' | 'HIGH';
  description?: string;
}

export interface ApprovalTask {
  id: string;
  name: string;
  applicationId: number;
  applicantName: string;
  applicantEmail: string;
  createTime: Date;
  stepType: string;
}

export interface ApprovalHistory {
  taskName: string;
  assigneeName: string;
  startTime: Date;
  endTime?: Date;
  duration: string;
  decision: string;
  comments: string;
  stepType: string;
  applicationId: number;
}

@Injectable({
  providedIn: 'root'
})
export class ApprovalService {

  private readonly apiUrl = `${environment.apiUrl}/approvals`;

  constructor(private http: HttpClient) { }

  /**
   * 获取所有审批请求（分页）
   */
  getAllApprovalRequests(page = 0, size = 20): Observable<ApiResponse<any>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get<ApiResponse<any>>(this.apiUrl, { params });
  }

  /**
   * 按状态获取审批请求
   */
  getApprovalRequestsByStatus(status: string, page = 0, size = 20): Observable<ApiResponse<any>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get<ApiResponse<any>>(`${this.apiUrl}/status/${status}`, { params });
  }

  /**
   * 获取待处理审批请求
   */
  getPendingApprovalRequests(page = 0, size = 20): Observable<ApiResponse<any>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get<ApiResponse<any>>(`${this.apiUrl}/pending`, { params });
  }

  /**
   * 获取审批请求详情
   */
  getApprovalRequest(id: number): Observable<ApiResponse<ApprovalRequest>> {
    return this.http.get<ApiResponse<ApprovalRequest>>(`${this.apiUrl}/${id}`);
  }

  /**
   * 处理审批请求
   */
  processApprovalRequest(requestId: number, decision: string, comments?: string): Observable<ApiResponse<any>> {
    const params = new HttpParams()
      .set('decision', decision)
      .set('comments', comments || '');
    
    return this.http.post<ApiResponse<any>>(`${this.apiUrl}/${requestId}/process`, null, { params });
  }

  /**
   * 获取审批统计
   */
  getApprovalStatistics(): Observable<ApiResponse<any>> {
    return this.http.get<ApiResponse<any>>(`${this.apiUrl}/statistics`);
  }

  /**
   * 删除审批请求
   */
  deleteApprovalRequest(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/${id}`);
  }
}