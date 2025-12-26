import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap, map } from 'rxjs/operators';
import { Router } from '@angular/router';
import { TokenRefreshService } from './token-refresh.service';
import { TokenStorageService } from './token-storage.service';
import { PermissionService } from './permission.service';
import { environment } from '../../environments/environment';
import { ApiResponse } from '../models/api.model';
import { TokenPair } from '../models/token.model';

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
  userType?: string;
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
          userType: undefined, // Will be populated on actual login
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
        } else if (response.code === 40301) {
          // Password expired - redirect to change password page
          // Store username for password change
          sessionStorage.setItem('pendingPasswordChange', credentials.username);
          this.router.navigate(['/change-password']);
          throw new Error(response.message || '密码已过期，请修改密码');
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

  isGuestUser(): boolean {
    const user = this.getCurrentUser();
    // Use userType if available, fallback to employeeId check
    if (user?.data?.userType) {
      return user.data.userType === 'GUEST';
    }
    // Fallback logic for backward compatibility
    return !user?.data?.employeeId;
  }

  isEmployeeUser(): boolean {
    const user = this.getCurrentUser();
    // Use userType if available, fallback to employeeId check
    if (user?.data?.userType) {
      return user.data.userType === 'EMPLOYEE';
    }
    // Fallback logic for backward compatibility
    return !!user?.data?.employeeId;
  }

  // Add getToken method for backward compatibility
  getToken(): string | null {
    return this.tokenStorage.getToken();
  }

  // Enhanced role detection methods
  getUserRole(): Observable<string> {
    return this.authState$.pipe(
      map(state => {
        if (!state.isAuthenticated || !state.user?.data) {
          return 'GUEST';
        }
        
        const userData = state.user.data;
        
        // Use userType if available, fallback to old logic for compatibility
        if (userData.userType) {
          return userData.userType;
        }
        
        // Fallback logic for backward compatibility
        // Check if user is a system user
        if (userData.isSystemUser) {
          return 'SYSTEM';
        }
        
        // Check if user has employee ID (employee vs guest)
        if (!userData.employeeId) {
          return 'GUEST';
        }
        
        return 'EMPLOYEE';
      })
    );
  }

  hasRole(role: string): Observable<boolean> {
    return this.getUserRole().pipe(
      map(userRole => userRole === role)
    );
  }

  isSystem(): Observable<boolean> {
    return this.hasRole('SYSTEM');
  }

  isEmployee(): Observable<boolean> {
    return this.hasRole('EMPLOYEE');
  }

  isGuest(): Observable<boolean> {
    return this.hasRole('GUEST');
  }

  // Enhanced permission checking
  canAccessHRModule(): Observable<boolean> {
    return this.getUserRole().pipe(
      map(role => role === 'SYSTEM' || role === 'EMPLOYEE')
    );
  }

  canAccessGuestModule(): Observable<boolean> {
    return this.getUserRole().pipe(
      map(role => role === 'GUEST' || role === 'SYSTEM')
    );
  }

  // Get user permissions
  getUserPermissions(): string[] {
    const user = this.getCurrentUser();
    return user?.data?.permissions || [];
  }
}
