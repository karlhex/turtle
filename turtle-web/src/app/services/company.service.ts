import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Company } from '../models/company.model';
import { environment } from '../../environments/environment';
import { Page } from '../models/page.model';
import { ApiResponse } from '../models/api.model';

@Injectable({
  providedIn: 'root'
})
export class CompanyService {
  private apiUrl = `${environment.apiUrl}/companies`;

  constructor(private http: HttpClient) {}

  getCompanies(params: { page: number; size: number; search?: string; active?: boolean }): Observable<ApiResponse<Page<Company>>> {
    let httpParams = new HttpParams()
      .set('page', params.page.toString())
      .set('size', params.size.toString());

    if (params.search) {
      httpParams = httpParams.set('search', params.search);
    }
    if (params.active !== undefined) {
      httpParams = httpParams.set('active', params.active.toString());
    }

    return this.http.get<ApiResponse<Page<Company>>>(this.apiUrl, { params: httpParams });
  }

  getCompany(id: number): Observable<ApiResponse<Company>> {
    return this.http.get<ApiResponse<Company>>(`${this.apiUrl}/${id}`);
  }

  createCompany(company: Company): Observable<ApiResponse<Company>> {
    return this.http.post<ApiResponse<Company>>(this.apiUrl, company);
  }

  updateCompany(id: number, company: Company): Observable<ApiResponse<Company>> {
    return this.http.put<ApiResponse<Company>>(`${this.apiUrl}/${id}`, company);
  }

  deleteCompany(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/${id}`);
  }

  toggleStatus(id: number): Observable<ApiResponse<Company>> {
    return this.http.put<ApiResponse<Company>>(`${this.apiUrl}/${id}/toggle-status`, {});
  }

  setPrimary(id: number): Observable<ApiResponse<Company>> {
    return this.http.put<ApiResponse<Company>>(`${this.apiUrl}/${id}/set-primary`, {});
  }

  getAllActive(): Observable<ApiResponse<Company[]>> {
    return this.http.get<ApiResponse<Company[]>>(`${this.apiUrl}/active`);
  }

  searchCompanies(query: string): Observable<ApiResponse<Company[]>> {
    const params = new HttpParams().set('query', query);
    return this.http.get<ApiResponse<Company[]>>(`${this.apiUrl}/search`, { params });
  }

  setPrimaryCompany(id: number): Observable<Company> {
    return this.http.put<Company>(`${this.apiUrl}/${id}/set-primary`, {});
  }
}
