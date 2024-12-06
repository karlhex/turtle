import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TaxInfo } from '../models/tax-info.model';
import { environment } from '../../environments/environment';
import { Page } from '../models/page.model';
import { ApiResponse } from '../models/api.model';

@Injectable({
  providedIn: 'root'
})
export class TaxInfoService {
  private apiUrl = `${environment.apiUrl}/tax-infos`;

  constructor(private http: HttpClient) {}

  getTaxInfos(params: { page: number; size: number; search?: string; active?: boolean }): Observable<ApiResponse<Page<TaxInfo>>> {
    let httpParams = new HttpParams()
      .set('page', params.page.toString())
      .set('size', params.size.toString());

    if (params.search) {
      httpParams = httpParams.set('search', params.search);
    }
    if (params.active !== undefined) {
      httpParams = httpParams.set('active', params.active.toString());
    }

    return this.http.get<ApiResponse<Page<TaxInfo>>>(this.apiUrl, { params: httpParams });
  }

  getTaxInfo(id: number): Observable<ApiResponse<TaxInfo>> {
    return this.http.get<ApiResponse<TaxInfo>>(`${this.apiUrl}/${id}`);
  }

  createTaxInfo(taxInfo: TaxInfo): Observable<ApiResponse<TaxInfo>> {
    return this.http.post<ApiResponse<TaxInfo>>(this.apiUrl, taxInfo);
  }

  updateTaxInfo(id: number, taxInfo: TaxInfo): Observable<ApiResponse<TaxInfo>> {
    return this.http.put<ApiResponse<TaxInfo>>(`${this.apiUrl}/${id}`, taxInfo);
  }

  deleteTaxInfo(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/${id}`);
  }

  toggleStatus(id: number): Observable<ApiResponse<TaxInfo>> {
    return this.http.put<ApiResponse<TaxInfo>>(`${this.apiUrl}/${id}/toggle-status`, {});
  }
}
