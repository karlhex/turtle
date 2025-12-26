import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse } from '../models/api.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SequenceService {
  private readonly API_URL = `${environment.apiUrl}/sequences`;

  constructor(private http: HttpClient) {}

  /**
   * 获取下一个序列号
   * @param type 序列类型 (如: EMPLOYEE, CONTRACT, PROJECT等)
   * @returns 格式化的序列号
   */
  getNextSequence(type: string): Observable<ApiResponse<string>> {
    return this.http.get<ApiResponse<string>>(`${this.API_URL}/next`, {
      params: { type }
    });
  }
}