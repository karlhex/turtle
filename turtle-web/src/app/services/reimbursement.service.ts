import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { 
  Reimbursement, 
  ReimbursementResponse, 
  CreateReimbursementRequest, 
  UpdateReimbursementRequest,
  ReimbursementSearchParams 
} from '../models/reimbursement.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
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
    if (params.applicantId) httpParams = httpParams.set('applicantId', params.applicantId.toString());
    if (params.approverId) httpParams = httpParams.set('approverId', params.approverId.toString());
    if (params.projectId) httpParams = httpParams.set('projectId', params.projectId.toString());

    return this.http.get<ReimbursementResponse>(this.apiUrl, { params: httpParams });
  }

  getById(id: number): Observable<Reimbursement> {
    return this.http.get<Reimbursement>(`${this.apiUrl}/${id}`);
  }

  getByReimbursementNo(reimbursementNo: string): Observable<Reimbursement> {
    return this.http.get<Reimbursement>(`${this.apiUrl}/no/${reimbursementNo}`);
  }

  create(request: CreateReimbursementRequest): Observable<Reimbursement> {
    return this.http.post<Reimbursement>(this.apiUrl, request);
  }

  update(id: number, request: UpdateReimbursementRequest): Observable<Reimbursement> {
    return this.http.put<Reimbursement>(`${this.apiUrl}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getByApplicant(applicantId: number): Observable<Reimbursement[]> {
    return this.http.get<Reimbursement[]>(`${this.apiUrl}/applicant/${applicantId}`);
  }

  getByApprover(approverId: number): Observable<Reimbursement[]> {
    return this.http.get<Reimbursement[]>(`${this.apiUrl}/approver/${approverId}`);
  }

  getByProject(projectId: number): Observable<Reimbursement[]> {
    return this.http.get<Reimbursement[]>(`${this.apiUrl}/project/${projectId}`);
  }

  getByDateRange(startDate: string, endDate: string, page: number = 0, size: number = 10): Observable<ReimbursementResponse> {
    const params = new HttpParams()
      .set('startDate', startDate)
      .set('endDate', endDate)
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<ReimbursementResponse>(`${this.apiUrl}/date-range`, { params });
  }
}
