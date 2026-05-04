import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse, AuthRes, LoginReq, RegisterReq } from '../models/api.model';

const KEY = 'commission_auth';

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(private http: HttpClient) {}

  login(payload: LoginReq): Observable<ApiResponse<AuthRes>> {
    return this.http.post<ApiResponse<AuthRes>>(`${environment.apiBaseUrl}/auth/login`, payload);
  }

  register(payload: RegisterReq): Observable<ApiResponse<AuthRes>> {
    return this.http.post<ApiResponse<AuthRes>>(`${environment.apiBaseUrl}/auth/register`, payload);
  }

  saveAuth(auth: AuthRes): void {
    localStorage.setItem(KEY, JSON.stringify(auth));
  }

  saveUser(auth: AuthRes): void {
    this.saveAuth(auth);
  }

  getAuth(): AuthRes | null {
    const raw = localStorage.getItem(KEY);
    return raw ? JSON.parse(raw) : null;
  }

  getUser(): AuthRes | null {
    return this.getAuth();
  }

  getToken(): string | null {
    return this.getAuth()?.accessToken || null;
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  logout(): void {
    localStorage.removeItem(KEY);
  }
}
