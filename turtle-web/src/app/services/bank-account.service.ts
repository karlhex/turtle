import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BankAccount } from '../models/bank-account.model';
import { environment } from '../../environments/environment';
import { Page } from '../models/page.model';
import { ApiResponse } from '../models/api.model';

@Injectable({
  providedIn: 'root'
})
export class BankAccountService {
  private apiUrl = `${environment.apiUrl}/bank-accounts`;

  constructor(private http: HttpClient) {}

  getBankAccounts(params: { page: number; size: number; search?: string; active?: boolean }): Observable<ApiResponse<Page<BankAccount>>> {
    let httpParams = new HttpParams()
      .set('page', params.page.toString())
      .set('size', params.size.toString());

    if (params.search) {
      httpParams = httpParams.set('search', params.search);
    }
    if (params.active !== undefined) {
      httpParams = httpParams.set('active', params.active.toString());
    }

    return this.http.get<ApiResponse<Page<BankAccount>>>(this.apiUrl, { params: httpParams });
  }

  getBankAccount(id: number): Observable<ApiResponse<BankAccount>> {
    return this.http.get<ApiResponse<BankAccount>>(`${this.apiUrl}/${id}`);
  }

  createBankAccount(bankAccount: BankAccount): Observable<ApiResponse<BankAccount>> {
    return this.http.post<ApiResponse<BankAccount>>(this.apiUrl, bankAccount);
  }

  updateBankAccount(id: number, bankAccount: BankAccount): Observable<ApiResponse<BankAccount>> {
    return this.http.put<ApiResponse<BankAccount>>(`${this.apiUrl}/${id}`, bankAccount);
  }

  deleteBankAccount(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/${id}`);
  }

  toggleStatus(id: number): Observable<ApiResponse<BankAccount>> {
    return this.http.put<ApiResponse<BankAccount>>(`${this.apiUrl}/${id}/toggle-status`, {});
  }
}
