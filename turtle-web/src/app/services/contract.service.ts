import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Contract } from '../models/contract.model';
import { environment } from '../../environments/environment';
import { Page } from '../models/page.model';
import { ApiResponse } from '../models/api.model';

@Injectable({
  providedIn: 'root',
})
export class ContractService {
  private apiUrl = `${environment.apiUrl}/contracts`;

  constructor(private http: HttpClient) {}

  getContracts(params: {
    page: number;
    size: number;
    search?: string;
  }): Observable<ApiResponse<Page<Contract>>> {
    let httpParams = new HttpParams()
      .set('page', params.page.toString())
      .set('size', params.size.toString());

    if (params.search) {
      httpParams = httpParams.set('search', params.search);
    }

    return this.http.get<ApiResponse<Page<Contract>>>(this.apiUrl, { params: httpParams });
  }

  getContract(id: number): Observable<ApiResponse<Contract>> {
    return this.http.get<ApiResponse<Contract>>(`${this.apiUrl}/${id}`);
  }

  createContract(contract: Contract): Observable<ApiResponse<Contract>> {
    return this.http.post<ApiResponse<Contract>>(this.apiUrl, contract);
  }

  updateContract(id: number, contract: Contract): Observable<ApiResponse<Contract>> {
    return this.http.put<ApiResponse<Contract>>(`${this.apiUrl}/${id}`, contract);
  }

  deleteContract(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/${id}`);
  }

  getContractsByProjectId(projectId: number): Observable<ApiResponse<Contract[]>> {
    return this.http.get<ApiResponse<Contract[]>>(`${this.apiUrl}/project/${projectId}`);
  }
}
