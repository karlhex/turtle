import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Project, ProjectQuery } from '../models/project.model';
import { environment } from '../../environments/environment';
import { Page } from '../models/page.model';
import { ApiResponse } from '../models/api.model';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  private apiUrl = `${environment.apiUrl}/projects`;

  constructor(private http: HttpClient) {}

  getProjects(params: ProjectQuery): Observable<ApiResponse<Page<Project>>> {
    let httpParams = new HttpParams()
      .set('page', params.page.toString())
      .set('size', params.size.toString());

    if (params.search) {
      httpParams = httpParams.set('search', params.search);
    }
    if (params.status) {
      httpParams = httpParams.set('status', params.status);
    }
    if (params.managerId) {
      httpParams = httpParams.set('managerId', params.managerId.toString());
    }
    if (params.startDate) {
      httpParams = httpParams.set('startDate', params.startDate.toISOString().split('T')[0]);
    }
    if (params.endDate) {
      httpParams = httpParams.set('endDate', params.endDate.toISOString().split('T')[0]);
    }

    return this.http.get<ApiResponse<Page<Project>>>(this.apiUrl, { params: httpParams });
  }

  getProject(id: number): Observable<ApiResponse<Project>> {
    return this.http.get<ApiResponse<Project>>(`${this.apiUrl}/${id}`);
  }

  createProject(project: Project): Observable<ApiResponse<Project>> {
    return this.http.post<ApiResponse<Project>>(this.apiUrl, project);
  }

  updateProject(id: number, project: Project): Observable<ApiResponse<Project>> {
    return this.http.put<ApiResponse<Project>>(`${this.apiUrl}/${id}`, project);
  }

  deleteProject(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/${id}`);
  }
}
