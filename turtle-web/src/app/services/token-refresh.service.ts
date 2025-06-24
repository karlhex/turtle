import { Injectable, OnDestroy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subscription, timer } from 'rxjs';
import { tap } from 'rxjs/operators';
import { TokenStorageService } from './token-storage.service';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class TokenRefreshService implements OnDestroy {
  private readonly REFRESH_URL = `${environment.apiUrl}/auth/refresh`;
  private refreshSubscription?: Subscription;
  // 设置刷新间隔为token过期时间的3/4
  private readonly REFRESH_INTERVAL = 15 * 60 * 1000; // 45分钟，假设token有效期为1小时

  constructor(private http: HttpClient, private tokenStorage: TokenStorageService) {}

  startRefreshTimer(): void {
    // 取消已存在的定时器
    this.stopRefreshTimer();

    // 创建新的定时器
    this.refreshSubscription = timer(this.REFRESH_INTERVAL, this.REFRESH_INTERVAL).subscribe(() => {
      this.refreshToken().subscribe({
        error: error => {
          console.error('Token refresh failed:', error);
          // 如果刷新失败，清除token
          this.tokenStorage.clear();
        },
      });
    });
  }

  stopRefreshTimer(): void {
    if (this.refreshSubscription) {
      this.refreshSubscription.unsubscribe();
    }
  }

  refreshToken(): Observable<any> {
    const refreshToken = this.tokenStorage.getRefreshToken();
    return this.http.post(this.REFRESH_URL, { token: refreshToken }).pipe(
      tap((response: any) => {
        if (response && response.data && response.data.tokenPair) {
          // 更新token
          this.tokenStorage.setTokenPair(response.data.tokenPair);
        } else {
          throw new Error('Invalid token response');
        }
      })
    );
  }

  ngOnDestroy(): void {
    this.stopRefreshTimer();
  }
}
