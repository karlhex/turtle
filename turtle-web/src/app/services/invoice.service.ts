import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Invoice } from '../models/invoice.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class InvoiceService {
  private readonly apiUrl = `${environment.apiUrl}/invoices`;

  constructor(private http: HttpClient) {}

  getById(id: number): Observable<Invoice> {
    return this.http.get<Invoice>(`${this.apiUrl}/${id}`);
  }

  getByInvoiceNo(invoiceNo: string): Observable<Invoice> {
    return this.http.get<Invoice>(`${this.apiUrl}/no/${invoiceNo}`);
  }

  getByBuyerTaxInfoId(buyerTaxInfoId: number): Observable<Invoice[]> {
    return this.http.get<Invoice[]>(`${this.apiUrl}/buyer/${buyerTaxInfoId}`);
  }

  getBySellerTaxInfoId(sellerTaxInfoId: number): Observable<Invoice[]> {
    return this.http.get<Invoice[]>(`${this.apiUrl}/seller/${sellerTaxInfoId}`);
  }

  getByBatchNo(batchNo: string): Observable<Invoice[]> {
    return this.http.get<Invoice[]>(`${this.apiUrl}/batch/${batchNo}`);
  }

  create(invoice: Invoice): Observable<Invoice> {
    return this.http.post<Invoice>(this.apiUrl, invoice);
  }

  update(id: number, invoice: Invoice): Observable<Invoice> {
    return this.http.put<Invoice>(`${this.apiUrl}/${id}`, invoice);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  cancel(id: number, reason: string): Observable<Invoice> {
    return this.http.post<Invoice>(`${this.apiUrl}/${id}/cancel`, null, {
      params: { reason },
    });
  }
}
