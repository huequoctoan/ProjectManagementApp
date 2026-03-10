import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthRequest, AuthResponse } from '../models/auth.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  // Đường dẫn gọi xuống Spring Boot
  private apiUrl = 'http://localhost:8080/api/auth/log-in';

  constructor(private http: HttpClient) { }

  // 1. Gọi API Đăng nhập
  login(request: AuthRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(this.apiUrl, request);
  }

  // 2. Cất Token vào két sắt (localStorage)
  setToken(token: string): void {
    localStorage.setItem('auth_token', token);
  }

  // 3. Lấy Token ra để dùng
  getToken(): string | null {
    return localStorage.getItem('auth_token');
  }

  // 4. Kiểm tra xem đã đăng nhập chưa
  isLoggedIn(): boolean {
    return !!this.getToken(); 
  }

  // 5. Đăng xuất (Xóa thẻ)
  logout(): void {
    localStorage.removeItem('auth_token');
  }
}