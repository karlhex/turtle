import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {
  private readonly TOKEN_KEY = 'access_token';
  private readonly USER_ID_KEY = 'user_id';
  private readonly USERNAME_KEY = 'username';
  
  private tokenSubject = new BehaviorSubject<string | null>(this.getToken());

  constructor() {}

  setToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
    this.tokenSubject.next(token);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
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

  clear(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_ID_KEY);
    localStorage.removeItem(this.USERNAME_KEY);
    this.tokenSubject.next(null);
  }

  getTokenObservable(): Observable<string | null> {
    return this.tokenSubject.asObservable();
  }
}
