import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { TokenRefreshService } from './token-refresh.service';
import { TokenStorageService } from './token-storage.service';
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

export interface SigninData {
  id: number;
  token: string;
  message?: string;
}

export type SigninResponse = ApiResponse<SigninData>;

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = `${environment.apiUrl}/auth`;
  private userSubject = new BehaviorSubject<SigninResponse | null>(null);
  
  constructor(
    private http: HttpClient,
    private router: Router,
    private tokenRefreshService: TokenRefreshService,
    private tokenStorage: TokenStorageService
  ) {
    // Check if user is already logged in
    const token = this.tokenStorage.getToken();
    const userId = this.tokenStorage.getUserId();
    if (token && userId) {
      this.userSubject.next({
        code: 200,
        data: {
          id: parseInt(userId),
          token
        }
      });
      // Start token refresh timer if user is logged in
      this.tokenRefreshService.startRefreshTimer();
    }
  }

  login(credentials: SigninRequest): Observable<SigninResponse> {
    console.log('signin url', `${this.API_URL}/signin`);
    return this.http.post<SigninResponse>(`${this.API_URL}/signin`, credentials)
      .pipe(
        tap(response => {
          console.log('Server response:', response);

          if (response && response.code === 200 && response.data && response.data.token) {
            // Store token and user info
            this.tokenStorage.setToken(response.data.token);
            this.tokenStorage.setUserId(response.data.id.toString());
            this.userSubject.next(response);
            // Start token refresh timer
            this.tokenRefreshService.startRefreshTimer();           
            // Navigate to dashboard
            this.router.navigate(['/dashboard']);
          } else {
            console.error('Invalid response format:', response);
            throw new Error('Invalid response from server');
          }
        })
      );
  }

  logout(): void {
    // Stop token refresh timer
    this.tokenRefreshService.stopRefreshTimer();
    // Clear storage
    this.tokenStorage.clear();
    this.userSubject.next(null);
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    return !!this.tokenStorage.getToken();
  }

  getCurrentUser(): SigninResponse | null {
    return this.userSubject.value;
  }

  getUserObservable(): Observable<SigninResponse | null> {
    return this.userSubject.asObservable();
  }

  // Add getToken method for backward compatibility
  getToken(): string | null {
    return this.tokenStorage.getToken();
  }
}
