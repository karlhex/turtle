import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse, PageResponse } from '../models/api.model';
import { Position } from '../models/position.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PositionService {
  private readonly API_URL = `${environment.apiUrl}/positions`;

  constructor(private http: HttpClient) {}

  getPositions(page: number = 0, size: number = 10): Observable<ApiResponse<PageResponse<Position>>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<ApiResponse<PageResponse<Position>>>(this.API_URL, { params });
  }

  getPositionById(id: number): Observable<ApiResponse<Position>> {
    return this.http.get<ApiResponse<Position>>(`${this.API_URL}/${id}`);
  }

  createPosition(position: Position): Observable<ApiResponse<Position>> {
    return this.http.post<ApiResponse<Position>>(this.API_URL, position);
  }

  updatePosition(id: number, position: Position): Observable<ApiResponse<Position>> {
    return this.http.put<ApiResponse<Position>>(`${this.API_URL}/${id}`, position);
  }

  deletePosition(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.API_URL}/${id}`);
  }

  getPositionsByDepartment(departmentId: number): Observable<ApiResponse<Position[]>> {
    return this.http.get<ApiResponse<Position[]>>(`${this.API_URL}/department/${departmentId}`);
  }

  getActivePositions(): Observable<ApiResponse<Position[]>> {
    return this.http.get<ApiResponse<Position[]>>(`${this.API_URL}/active`);
  }
}
