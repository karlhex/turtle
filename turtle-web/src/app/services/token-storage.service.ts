import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { TokenPair } from '../models/token.model';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {
  private readonly TOKEN_KEY = 'access_token';
  private readonly REFRESH_TOKEN_KEY = 'refresh_token';
  private readonly EXPIRES_IN_KEY = 'expires_in';
  private readonly EXPIRES_IN_REFRESH_KEY = 'expires_in_refresh';
  private readonly USER_ID_KEY = 'user_id';
  private readonly USERNAME_KEY = 'username';
  private readonly USER_INFO_KEY = 'user_info';

  private tokenSubject = new BehaviorSubject<string | null>(this.getToken());
  private userInfoSubject = new BehaviorSubject<any>(this.getUserInfo());

  constructor() {}

  setToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
    this.tokenSubject.next(token);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  setRefreshToken(token: string): void {
    localStorage.setItem(this.REFRESH_TOKEN_KEY, token);
  }

  getRefreshToken(): string | null {
    return localStorage.getItem(this.REFRESH_TOKEN_KEY);
  }

  setExpiresIn(expiresIn: number): void {
    localStorage.setItem(this.EXPIRES_IN_KEY, expiresIn.toString());
  }

  getExpiresIn(): number | null {
    const expiresIn = localStorage.getItem(this.EXPIRES_IN_KEY);
    return expiresIn ? parseInt(expiresIn) : null;
  }

  setExpiresInRefresh(expiresIn: number): void {
    localStorage.setItem(this.EXPIRES_IN_REFRESH_KEY, expiresIn.toString());
  }

  getExpiresInRefresh(): number | null {
    const expiresIn = localStorage.getItem(this.EXPIRES_IN_REFRESH_KEY);
    return expiresIn ? parseInt(expiresIn) : null;
  }
  
  setUserId(id: string): void {
    localStorage.setItem(this.USER_ID_KEY, id);
  }

  getUserId(): string | null {
    return localStorage.getItem(this.USER_ID_KEY);
  }

  setUsername(username: string): void {
    localStorage.setItem(this.USERNAME_KEY, username);
  }

  getUsername(): string | null {
    return localStorage.getItem(this.USERNAME_KEY);
  }

  setUserInfo(userInfo: any): void {
    if (userInfo) {
      localStorage.setItem(this.USER_INFO_KEY, JSON.stringify(userInfo));
    } else {
      localStorage.removeItem(this.USER_INFO_KEY);
    }
    this.userInfoSubject.next(userInfo);
  }

  getUserInfo(): any {
    const userInfo = localStorage.getItem(this.USER_INFO_KEY);
    return userInfo ? JSON.parse(userInfo) : null;
  }

  getUserInfoObservable(): Observable<any> {
    return this.userInfoSubject.asObservable();
  }

  clear(): void {
    localStorage.clear();
    this.tokenSubject.next(null);
    this.userInfoSubject.next(null);
  }

  getTokenObservable(): Observable<string | null> {
    return this.tokenSubject.asObservable();
  }

  setTokenPair(tokenPair: TokenPair): void {
    this.setToken(tokenPair.accessToken);
    this.setExpiresIn(tokenPair.accessTokenExpiry);
    this.setRefreshToken(tokenPair.refreshToken);
    this.setExpiresInRefresh(tokenPair.refreshTokenExpiry); 
  }

  getTokenPair(): TokenPair | null {
    return {
      accessToken: this.getToken() || '',
      accessTokenExpiry: this.getExpiresIn() || 0,
      refreshToken: this.getRefreshToken() || '',
      refreshTokenExpiry: this.getExpiresInRefresh() || 0
    }
  }

}
