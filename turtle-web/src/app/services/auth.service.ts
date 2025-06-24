import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap, map } from 'rxjs/operators';
import { Router } from '@angular/router';
import { TokenRefreshService } from './token-refresh.service';
import { TokenStorageService } from './token-storage.service';
import { PermissionService } from './permission.service';
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
  permissions: any;
}

export type SigninResponse = ApiResponse<SigninData>;

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly API_URL = `${environment.apiUrl}/auth`;
  private userSubject = new BehaviorSubject<SigninResponse | null>(null);
  public authState$ = this.userSubject.asObservable().pipe(
    map(user => ({
      isAuthenticated: !!user,
      user,
    }))
  );

  constructor(
    private http: HttpClient,
    private router: Router,
    private tokenStorage: TokenStorageService,
    private permissionService: PermissionService
  ) {
    // Check if user is already logged in
    const tokenPair = this.tokenStorage.getTokenPair();
    const userId = this.tokenStorage.getUserId();
    const permissions = this.tokenStorage.getStoredPermissions();

    if (tokenPair && userId) {
      // 如果有存储的权限，先恢复它们
      if (permissions) {
        this.permissionService.setPermissions(permissions);
      }

      this.userSubject.next({
        code: 200,
        data: {
          id: parseInt(userId),
          tokenPair: tokenPair,
          employeeId: 0,
          employeeName: '',
          employeeDepartment: '',
          employeePosition: '',
          isSystemUser: false,
          permissions: permissions || [],
        },
        message: '',
      });
    }
  }

  login(credentials: SigninRequest): Observable<SigninResponse> {
    return this.http.post<SigninResponse>(`${this.API_URL}/signin`, credentials).pipe(
      map(response => {
        if (response.code === 200 && response.data) {
          const { tokenPair, permissions, ...userInfo } = response.data;
          this.tokenStorage.setTokenPair(tokenPair);
          this.tokenStorage.setUserInfo(userInfo);
          // 存储权限到 localStorage
          this.permissionService.setPermissions(permissions);
          this.userSubject.next(response);

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
    const token = this.tokenStorage.getToken();
    if (token) {
      this.http.post<any>(`${this.API_URL}/logout`, { accessToken: token }).subscribe();
    }
    this.tokenStorage.clear();
    this.permissionService.clearPermissions();
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
