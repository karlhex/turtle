import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Contact } from '../models/contact.model';
import { ApiResponse } from '../models/api.model';
import { Page } from '../models/page.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ContactService {
  private apiUrl = `${environment.apiUrl}/contacts`;

  constructor(private http: HttpClient) {}

  /**
   * Get paginated contacts
   * @param page Page number
   * @param size Page size
   * @param searchText Optional search text
   */
  getContacts(page: number, size: number, searchText?: string): Observable<ApiResponse<Page<Contact>>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (searchText) {
      return this.searchContacts(searchText, page, size);
    }

    return this.http.get<ApiResponse<Page<Contact>>>(this.apiUrl, { params });
  }

  /**
   * Search contacts
   * @param query Search query
   * @param page Page number
   * @param size Page size
   */
  private searchContacts(query: string, page: number, size: number): Observable<ApiResponse<Page<Contact>>> {
    const params = new HttpParams()
      .set('query', query)
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<ApiResponse<Page<Contact>>>(`${this.apiUrl}/search`, { params });
  }

  /**
   * Get contact by ID
   * @param id Contact ID
   */
  getContact(id: number): Observable<ApiResponse<Contact>> {
    return this.http.get<ApiResponse<Contact>>(`${this.apiUrl}/${id}`);
  }

  /**
   * Create new contact
   * @param contact Contact data
   */
  createContact(contact: Contact): Observable<ApiResponse<Contact>> {
    return this.http.post<ApiResponse<Contact>>(this.apiUrl, contact);
  }

  /**
   * Update contact
   * @param id Contact ID
   * @param contact Contact data
   */
  updateContact(id: number, contact: Contact): Observable<ApiResponse<Contact>> {
    return this.http.put<ApiResponse<Contact>>(`${this.apiUrl}/${id}`, contact);
  }

  /**
   * Delete contact
   * @param id Contact ID
   */
  deleteContact(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/${id}`);
  }

  /**
   * Get contacts by company ID
   * @param companyId Company ID
   */
  getContactsByCompany(companyId: number): Observable<ApiResponse<Contact[]>> {
    return this.http.get<ApiResponse<Contact[]>>(`${this.apiUrl}/company/${companyId}`);
  }

  /**
   * Get all active contacts
   */
  getAllActiveContacts(): Observable<ApiResponse<Contact[]>> {
    return this.http.get<ApiResponse<Contact[]>>(`${this.apiUrl}/active`);
  }

  /**
   * Toggle contact status
   * @param id Contact ID
   */
  toggleStatus(id: number): Observable<ApiResponse<Contact>> {
    return this.http.put<ApiResponse<Contact>>(`${this.apiUrl}/${id}/toggle-status`, {});
  }
}
