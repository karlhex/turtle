import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import {
  Reimbursement,
  ReimbursementResponse,
  CreateReimbursementRequest,
  UpdateReimbursementRequest,
  ReimbursementSearchParams,
} from '../models/reimbursement.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ReimbursementService {
  private apiUrl = `${environment.apiUrl}/reimbursements`;

  constructor(private http: HttpClient) {}

  getAll(params: ReimbursementSearchParams): Observable<ReimbursementResponse> {
    let httpParams = new HttpParams();

    if (params.page !== undefined) httpParams = httpParams.set('page', params.page.toString());
    if (params.size !== undefined) httpParams = httpParams.set('size', params.size.toString());
    if (params.sort !== undefined) httpParams = httpParams.set('sort', params.sort);
    if (params.startDate) httpParams = httpParams.set('startDate', params.startDate);
    if (params.endDate) httpParams = httpParams.set('endDate', params.endDate);
    if (params.applicantId)
      httpParams = httpParams.set('applicantId', params.applicantId.toString());
    if (params.approverId) httpParams = httpParams.set('approverId', params.approverId.toString());
    if (params.projectId) httpParams = httpParams.set('projectId', params.projectId.toString());

    return this.http.get<any>(this.apiUrl, { params: httpParams })
      .pipe(map(response => response.data));
  }

  getById(id: number): Observable<Reimbursement> {
    return this.http.get<any>(`${this.apiUrl}/${id}`)
      .pipe(map(response => response.data));
  }

  getByReimbursementNo(reimbursementNo: string): Observable<Reimbursement> {
    return this.http.get<any>(`${this.apiUrl}/no/${reimbursementNo}`)
      .pipe(map(response => response.data));
  }

  create(request: CreateReimbursementRequest): Observable<Reimbursement> {
    return this.http.post<any>(this.apiUrl, request)
      .pipe(map(response => response.data));
  }

  update(id: number, request: UpdateReimbursementRequest): Observable<Reimbursement> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, request)
      .pipe(map(response => response.data));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<any>(`${this.apiUrl}/${id}`)
      .pipe(map(response => response.data));
  }

  submitForApproval(id: number): Observable<Reimbursement> {
    return this.http.post<any>(`${this.apiUrl}/${id}/submit`, {})
      .pipe(map(response => response.data));
  }

  getByApplicant(applicantId: number): Observable<Reimbursement[]> {
    return this.http.get<any>(`${this.apiUrl}/applicant/${applicantId}`)
      .pipe(map(response => response.data));
  }

  getByApprover(approverId: number): Observable<Reimbursement[]> {
    return this.http.get<any>(`${this.apiUrl}/approver/${approverId}`)
      .pipe(map(response => response.data));
  }

  getByProject(projectId: number): Observable<Reimbursement[]> {
    return this.http.get<any>(`${this.apiUrl}/project/${projectId}`)
      .pipe(map(response => response.data));
  }

  getByDateRange(
    startDate: string,
    endDate: string,
    page = 0,
    size = 10
  ): Observable<ReimbursementResponse> {
    const params = new HttpParams()
      .set('startDate', startDate)
      .set('endDate', endDate)
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<any>(`${this.apiUrl}/date-range`, { params })
      .pipe(map(response => response.data));
  }

  approve(id: number): Observable<Reimbursement> {
    return this.http.post<any>(`${this.apiUrl}/${id}/approve`, {})
      .pipe(map(response => response.data));
  }

  reject(id: number, reason: string): Observable<Reimbursement> {
    const params = new HttpParams().set('reason', reason);
    return this.http.post<any>(`${this.apiUrl}/${id}/reject`, {}, { params })
      .pipe(map(response => response.data));
  }

  resubmit(id: number): Observable<Reimbursement> {
    return this.http.post<any>(`${this.apiUrl}/${id}/resubmit`, {})
      .pipe(map(response => response.data));
  }

  // Reimbursement-specific business methods only
  // Workflow operations are now handled by WorkflowService
}
