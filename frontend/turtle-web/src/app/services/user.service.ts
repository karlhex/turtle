import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ApiResponse } from '../models/api.model';
import { ChangePasswordRequest } from '../models/change-password-request.model';

export interface Role {
  id: number;
  name: string;
  description?: string;
}

export interface User {
  id: number;
  username: string;
  email: string;
  status: string;
  roles: Role[];
  roleNames: string[];
  createdAt?: Date;
  updatedAt?: Date;
  passwordExpired?: boolean;
  accountLocked?: boolean;
  accountLockedUntil?: Date;
}

export interface CreateUserDto {
  username: string;
  email: string;
  password: string;
  status?: string;
  roleNames: string[];
}

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private readonly API_URL = `${environment.apiUrl}/users`;

  constructor(private http: HttpClient) {}

  getUsers(
    page = 0,
    pageSize = 10,
    sort?: { sortBy: string; direction: 'ASC' | 'DESC' }
  ): Observable<ApiResponse<any>> {
    let url = `${this.API_URL}?page=${page}&size=${pageSize}`;
    if (sort) {
      url += `&sort=${sort.sortBy},${sort.direction}`;
    }
    return this.http.get<ApiResponse<any>>(url);
  }

  getUser(id: number): Observable<ApiResponse<User>> {
    return this.http.get<ApiResponse<User>>(`${this.API_URL}/${id}`);
  }

  getUnmappedUsers(): Observable<ApiResponse<User[]>> {
    return this.http.get<ApiResponse<User[]>>(`${this.API_URL}/unmapped`);
  }

  updateUser(id: number, user: User): Observable<ApiResponse<User>> {
    return this.http.put<ApiResponse<User>>(`${this.API_URL}/${id}`, user);
  }

  deleteUser(id: number): Observable<ApiResponse<User>> {
    return this.http.delete<ApiResponse<User>>(`${this.API_URL}/${id}`);
  }

  createUser(user: CreateUserDto): Observable<ApiResponse<User>> {
    return this.http.post<ApiResponse<User>>(this.API_URL, user);
  }

  searchUsers(
    query: string,
    page = 0,
    pageSize = 10
  ): Observable<ApiResponse<any>> {
    const url = `${this.API_URL}/search?query=${encodeURIComponent(
      query
    )}&page=${page}&size=${pageSize}`;
    return this.http.get<ApiResponse<any>>(url);
  }

  changeUserPassword(changePasswordRequest: ChangePasswordRequest): Observable<ApiResponse<void>> {
    return this.http.post<ApiResponse<void>>(
      `${this.API_URL}/change-password`,
      changePasswordRequest
    );
  }

  resetPassword(userId: number): Observable<ApiResponse<string>> {
    return this.http.post<ApiResponse<string>>(`${this.API_URL}/${userId}/reset-password`, {});
  }

  forcePasswordChange(userId: number): Observable<ApiResponse<void>> {
    return this.http.post<ApiResponse<void>>(`${this.API_URL}/${userId}/force-password-change`, {});
  }

  isPasswordExpired(userId: number): Observable<ApiResponse<boolean>> {
    return this.http.get<ApiResponse<boolean>>(`${this.API_URL}/${userId}/password-expired`);
  }

  isAccountLocked(username: string): Observable<ApiResponse<boolean>> {
    return this.http.get<ApiResponse<boolean>>(`${this.API_URL}/account-locked/${username}`);
  }
}
