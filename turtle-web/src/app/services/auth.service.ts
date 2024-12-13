import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';

export interface SigninRequest {
  username: string;
  password: string;
}

export interface ApiResponse<T> {
  code: number;
  message?: string;
  data: T;
}

export interface User {
  id: number;
  username: string;
  email: string;
  roleNames?: string[];
  createdAt?: string;
  updatedAt?: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = `${environment.apiUrl}/`;
  private userSubject = new BehaviorSubject<User | null>(null);
  private credentialsKey = 'auth_credentials';
  
  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    // Check if user is already logged in
    const credentials = this.getCredentials();
    if (credentials) {
      // Validate credentials by making a test request
      this.validateCredentials().subscribe({
        error: () => {
          // If validation fails, clear credentials
          this.logout();
        }
      });
    }
  }

  login(credentials: SigninRequest): Observable<ApiResponse<User>> {
    const base64Credentials = btoa(`${credentials.username}:${credentials.password}`);
    localStorage.setItem(this.credentialsKey, base64Credentials);

    return this.validateCredentials();
  }

  private validateCredentials(): Observable<ApiResponse<User>> {
    // Make a request to a protected endpoint to validate credentials
    return this.http.get<ApiResponse<User>>(`${environment.apiUrl}/users/me`).pipe(
      tap(response => {
        if (response && response.code === 200 && response.data) {
          this.userSubject.next(response.data);
          this.router.navigate(['/dashboard']);
        } else {
          this.logout();
          throw new Error('Invalid response from server');
        }
      })
    );
  }

  signup(signupRequest: any): Observable<ApiResponse<User>> {
    return this.http.post<ApiResponse<User>>(`${this.API_URL}/signup`, signupRequest);
  }

  logout(): void {
    localStorage.removeItem(this.credentialsKey);
    this.userSubject.next(null);
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    return !!this.getCredentials();
  }

  getCurrentUser(): User | null {
    return this.userSubject.value;
  }

  getUserObservable(): Observable<User | null> {
    return this.userSubject.asObservable();
  }

  getCredentials(): string | null {
    return localStorage.getItem(this.credentialsKey);
  }
}
