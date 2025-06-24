import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Inventory } from '../models/inventory.model';
import { ApiResponse, PageResponse } from '../models/api.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class InventoryService {
  private apiUrl = `${environment.apiUrl}/inventories`;

  constructor(private http: HttpClient) {}

  create(inventory: Inventory): Observable<ApiResponse<Inventory>> {
    return this.http.post<ApiResponse<Inventory>>(this.apiUrl, inventory);
  }

  getById(id: number): Observable<ApiResponse<Inventory>> {
    return this.http.get<ApiResponse<Inventory>>(`${this.apiUrl}/${id}`);
  }

  getAll(
    page: number = 0,
    size: number = 10,
    sort: string = 'id,desc'
  ): Observable<ApiResponse<PageResponse<Inventory>>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);

    return this.http.get<ApiResponse<PageResponse<Inventory>>>(this.apiUrl, { params });
  }

  outbound(id: number, inventory: Inventory): Observable<ApiResponse<Inventory>> {
    return this.http.put<ApiResponse<Inventory>>(`${this.apiUrl}/${id}/outbound`, inventory);
  }
  borrow(id: number, inventory: Inventory): Observable<ApiResponse<Inventory>> {
    return this.http.put<ApiResponse<Inventory>>(`${this.apiUrl}/${id}/borrow`, inventory);
  }
  return(id: number, inventory: Inventory): Observable<ApiResponse<Inventory>> {
    return this.http.put<ApiResponse<Inventory>>(`${this.apiUrl}/${id}/return`, inventory);
  }

  delete(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/${id}`);
  }

  getBulkByIds(ids: number[]): Observable<ApiResponse<Inventory[]>> {
    return this.http.post<ApiResponse<Inventory[]>>(`${this.apiUrl}/bulk`, ids);
  }
}
