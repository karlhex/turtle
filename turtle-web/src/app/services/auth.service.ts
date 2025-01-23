import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap, map } from 'rxjs/operators';
import { Router } from '@angular/router';
import { TokenRefreshService } from './token-refresh.service';
import { TokenStorageService } from './token-storage.service';
import { environment } from '../../environments/environment';
import { ApiResponse } from '@app/models/api.model';
import { TokenPair } from '@app/models/token.model';

export interface SigninRequest {
  username: string;
  password: string;
}

export interface SigninData {
  id: number;
  tokenPair: TokenPair;
  employeeId?: number;
  employeeName?: string;
  employeeDepartment?: string;
  employeePosition?: string;
  isSystemUser?: boolean;  
}

export type SigninResponse = ApiResponse<SigninData>;

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = `${environment.apiUrl}/auth`;
  private userSubject = new BehaviorSubject<SigninResponse | null>(null);
  public authState$ = this.userSubject.asObservable().pipe(
    map(user => ({
      isAuthenticated: !!user,
      user
    }))
  );
  
  constructor(
    private http: HttpClient,
    private router: Router,
    private tokenRefreshService: TokenRefreshService,
    private tokenStorage: TokenStorageService
  ) {
    // Check if user is already logged in
    const tokenPair = this.tokenStorage.getTokenPair();
    const userId = this.tokenStorage.getUserId();
    if (tokenPair && userId) {
      this.userSubject.next({
        code: 200,
        data: {
          id: parseInt(userId),
          tokenPair: tokenPair,
          employeeId: 0,
          employeeName: '',
          employeeDepartment: '',
          employeePosition: '',
          isSystemUser: false
        },
        message: ''
      });
      // Start token refresh timer if user is logged in
      // this.tokenRefreshService.startRefreshTimer();
    }
  }

  login(credentials: SigninRequest): Observable<SigninResponse> {
    return this.http.post<SigninResponse>(`${this.API_URL}/signin`, credentials)
      .pipe(
        map(response => {
          if (response.code === 200 && response.data) {
            const { tokenPair, ...userInfo } = response.data;
            this.tokenStorage.setTokenPair(tokenPair);
            this.tokenStorage.setUserInfo(userInfo);
            this.userSubject.next(response);
            // Start token refresh timer
            // this.tokenRefreshService.startRefreshTimer();           
            // Navigate based on employee status
            if (response.data.employeeId) {
              this.router.navigate(['/dashboard']);
            } else {
              this.router.navigate(['/guest-dashboard']);
            }
            return response;
          }
          throw new Error(response.message || 'Login failed');
        })
      );
  }

  logout(): void {
    const tokenPair = this.tokenStorage.getTokenPair();
    if (tokenPair) {
      this.http.post<any>(`${this.API_URL}/logout`, { accessToken: tokenPair.accessToken }).subscribe();
    }
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
