import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ApiResponse } from '../models/api-response.model';
import { EmployeeApplication, ApplicationStatus } from '../models/employee-application.model';
import { ApplicationStatistics } from '../models/application-statistics.model';
import { Employee } from '../models/employee.model';

@Injectable({
  providedIn: 'root'
})
export class EmployeeApplicationService {

  private readonly apiUrl = `${environment.apiUrl}/employee-applications`;

  constructor(private http: HttpClient) { }

  /**
   * 提交入职申请（GUEST用户）
   */
  submitApplication(application: EmployeeApplication): Observable<ApiResponse<EmployeeApplication>> {
    return this.http.post<ApiResponse<EmployeeApplication>>(this.apiUrl, application);
  }

  /**
   * 更新入职申请
   */
  updateApplication(id: number, application: EmployeeApplication): Observable<ApiResponse<EmployeeApplication>> {
    return this.http.put<ApiResponse<EmployeeApplication>>(`${this.apiUrl}/${id}`, application);
  }

  /**
   * 获取申请详情
   */
  getApplication(id: number): Observable<ApiResponse<EmployeeApplication>> {
    return this.http.get<ApiResponse<EmployeeApplication>>(`${this.apiUrl}/${id}`);
  }

  /**
   * 获取所有申请（分页）
   */
  getAllApplications(page = 0, size = 20): Observable<ApiResponse<any>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get<ApiResponse<any>>(this.apiUrl, { params });
  }

  /**
   * 按状态获取申请
   */
  getApplicationsByStatus(status: ApplicationStatus, page = 0, size = 20): Observable<ApiResponse<any>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get<ApiResponse<any>>(`${this.apiUrl}/status/${status}`, { params });
  }

  /**
   * 获取待处理申请
   */
  getPendingApplications(page = 0, size = 20): Observable<ApiResponse<any>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get<ApiResponse<any>>(`${this.apiUrl}/pending`, { params });
  }

  /**
   * 获取我的申请（GUEST用户）
   */
  getMyApplications(): Observable<ApiResponse<EmployeeApplication[]>> {
    return this.http.get<ApiResponse<EmployeeApplication[]>>(`${this.apiUrl}/my-applications`);
  }

  /**
   * 审核申请
   */
  reviewApplication(reviewData: { applicationId: number; status: ApplicationStatus; reviewComments?: string | null }): Observable<ApiResponse<EmployeeApplication>> {
    const { applicationId, status, reviewComments } = reviewData;
    const params = new HttpParams()
      .set('status', status)
      .set('comments', reviewComments || '');
    
    return this.http.post<ApiResponse<EmployeeApplication>>(`${this.apiUrl}/${applicationId}/review`, null, { params });
  }

  /**
   * 批准并转为员工
   */
  approveAndConvertToEmployee(id: number, employeeInfo: Employee): Observable<ApiResponse<Employee>> {
    return this.http.post<ApiResponse<Employee>>(`${this.apiUrl}/${id}/approve-and-convert`, employeeInfo);
  }

  /**
   * 删除申请
   */
  deleteApplication(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/${id}`);
  }

  /**
   * 获取申请统计
   */
  getApplicationStatistics(): Observable<ApiResponse<ApplicationStatistics>> {
    return this.http.get<ApiResponse<ApplicationStatistics>>(`${this.apiUrl}/statistics`);
  }

  /**
   * 检查身份证号是否已存在
   */
  checkIdNumber(idNumber: string): Observable<ApiResponse<boolean>> {
    return this.http.get<ApiResponse<boolean>>(`${this.apiUrl}/check-id-number/${idNumber}`);
  }

  /**
   * 检查邮箱是否有待处理申请
   */
  checkPendingEmail(email: string): Observable<ApiResponse<boolean>> {
    return this.http.get<ApiResponse<boolean>>(`${this.apiUrl}/check-pending-email/${email}`);
  }

  // 工作流相关方法
  getMyPendingTasks(): Observable<ApiResponse<any[]>> {
    return this.http.get<ApiResponse<any[]>>(`${this.apiUrl}/my-pending-tasks`);
  }

  getApprovalHistory(applicationId: number): Observable<ApiResponse<any[]>> {
    return this.http.get<ApiResponse<any[]>>(`${this.apiUrl}/${applicationId}/approval-history`);
  }

  getCurrentApprovalTask(applicationId: number): Observable<ApiResponse<any>> {
    return this.http.get<ApiResponse<any>>(`${this.apiUrl}/${applicationId}/current-task`);
  }
}