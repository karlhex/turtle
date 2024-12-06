import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Currency, CurrencyQuery } from '../models/currency.model';
import { environment } from '../../environments/environment';
import { Page } from '../models/page.model';
import { ApiResponse } from '../models/api.model';

@Injectable({
  providedIn: 'root'
})
export class CurrencyService {
  private apiUrl = `${environment.apiUrl}/currencies`;

  constructor(private http: HttpClient) {}

  getCurrencies(params: CurrencyQuery): Observable<ApiResponse<Page<Currency>>> {
    let httpParams = new HttpParams()
      .set('page', params.page.toString())
      .set('size', params.size.toString());

    if (params.search) {
      httpParams = httpParams.set('search', params.search);
    }
    if (params.active !== undefined) {
      httpParams = httpParams.set('active', params.active.toString());
    }

    return this.http.get<ApiResponse<Page<Currency>>>(this.apiUrl, { params: httpParams });
  }

  getCurrency(id: number): Observable<ApiResponse<Currency>> {
    return this.http.get<ApiResponse<Currency>>(`${this.apiUrl}/${id}`);
  }

  createCurrency(currency: Currency): Observable<ApiResponse<Currency>> {
    return this.http.post<ApiResponse<Currency>>(this.apiUrl, currency);
  }

  updateCurrency(id: number, currency: Currency): Observable<ApiResponse<Currency>> {
    return this.http.put<ApiResponse<Currency>>(`${this.apiUrl}/${id}`, currency);
  }

  deleteCurrency(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/${id}`);
  }

  toggleStatus(id: number): Observable<ApiResponse<Currency>> {
    return this.http.put<ApiResponse<Currency>>(`${this.apiUrl}/${id}/toggle-status`, {});
  }

  setBaseCurrency(id: number): Observable<ApiResponse<Currency>> {
    return this.http.put<ApiResponse<Currency>>(`${this.apiUrl}/${id}/set-base`, {});
  }
}
