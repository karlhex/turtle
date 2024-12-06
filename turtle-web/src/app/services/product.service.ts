import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Product } from '../models/product.model';
import { ProductType } from '../types/product-type.enum';
import { ApiResponse } from '../models/api.model';
import { Page } from '../models/page.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = `${environment.apiUrl}/products`;

  constructor(private http: HttpClient) {}

  create(product: Product): Observable<ApiResponse<Product>> {
    console.log("create product", product);
    return this.http.post<ApiResponse<Product>>(this.apiUrl, product);
  }

  update(id: number, product: Product): Observable<ApiResponse<Product>> {
    return this.http.put<ApiResponse<Product>>(`${this.apiUrl}/${id}`, product);
  }

  delete(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/${id}`);
  }

  getById(id: number): Observable<Product> {
    return this.http.get<ApiResponse<Product>>(`${this.apiUrl}/${id}`)
      .pipe(map(response => response.data));
  }

  getProducts(page = 0, size = 10, sortBy = 'id', direction = 'ASC'): Observable<ApiResponse<Page<Product>>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('direction', direction);

    return this.http.get<ApiResponse<Page<Product>>>(this.apiUrl, { params });
  }

  getAllActive(page = 0, size = 10, sortBy = 'id', direction = 'ASC'): Observable<ApiResponse<Product>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('direction', direction);

    return this.http.get<ApiResponse<Product>>(`${this.apiUrl}/active`, { params });
  }

  search(query: string, page = 0, size = 10, sortBy = 'id', direction = 'ASC'): Observable<ApiResponse<Page<Product>>> {
    const params = new HttpParams()
      .set('query', query)
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('direction', direction);

    return this.http.get<ApiResponse<Page<Product>>>(`${this.apiUrl}/search`, { params });
  }

  getByType(type: ProductType, page = 0, size = 10, sortBy = 'id', direction = 'ASC'): Observable<ApiResponse<Page<Product>>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('direction', direction);

    return this.http.get<ApiResponse<Page<Product>>>(`${this.apiUrl}/type/${type}`, { params });
  }

  toggleStatus(id: number): Observable<ApiResponse<void>> {
    return this.http.put<ApiResponse<void>>(`${this.apiUrl}/${id}/toggle-status`, null);
  }
}
