import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse, PageResponse } from '../models/api.model';
import { Person } from '../models/person.model';

@Injectable({
  providedIn: 'root'
})
export class PersonService {
  private readonly API_URL = 'http://localhost:8080/api/persons';

  constructor(private http: HttpClient) {}

  getPersons(page: number = 0, size: number = 10): Observable<ApiResponse<PageResponse<Person>>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<ApiResponse<PageResponse<Person>>>(this.API_URL, { params });
  }

  searchPersons(query: string): Observable<ApiResponse<Person[]>> {
    const params = new HttpParams().set('query', query);
    return this.http.get<ApiResponse<Person[]>>(`${this.API_URL}/search`, { params });
  }

  getAll(): Observable<ApiResponse<Person[]>> {
    return this.http.get<ApiResponse<Person[]>>(`${this.API_URL}/all`);
  }
}
