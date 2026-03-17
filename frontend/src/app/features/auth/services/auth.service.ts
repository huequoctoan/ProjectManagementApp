import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { AuthRequest, AuthResponse } from '../models/auth.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  // Đường dẫn gọi xuống Spring Boot
  private apiUrl = 'http://localhost:8080/api/auth/log-in';

  private currentUserSubject = new BehaviorSubject<Partial<AuthResponse>>({
    userId: this.getUserId() || undefined,
    username: this.getUsername() || undefined,
    fullName: this.getFullName() || undefined,
    email: this.getEmail() || undefined,
    avatar: this.getAvatar() || undefined
  });

  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) { }

  // 1. Gọi API Đăng nhập
  login(request: AuthRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(this.apiUrl, request);
  }

  // 2. Cất Token vào két sắt (localStorage)
  setToken(token: string): void {
    localStorage.setItem('auth_token', token);
  }

  setUserId(userId: number): void {
    localStorage.setItem('auth_userId', userId.toString());
  }

  setUsername(username: string): void {
    localStorage.setItem('auth_username', username);
    this.updateSubject();
  }

  setFullName(fullName: string): void {
    localStorage.setItem('auth_fullName', fullName);
    this.updateSubject();
  }

  setEmail(email: string): void {
    localStorage.setItem('auth_email', email);
    this.updateSubject();
  }

  setAvatar(avatar: string): void {
    localStorage.setItem('auth_avatar', avatar);
    this.updateSubject();
  }
  
  private updateSubject(): void {
    this.currentUserSubject.next({
      userId: this.getUserId() || undefined,
      username: this.getUsername() || undefined,
      fullName: this.getFullName() || undefined,
      email: this.getEmail() || undefined,
      avatar: this.getAvatar() || undefined
    });
  }

  // 3. Lấy Token ra để dùng
  getToken(): string | null {
    return localStorage.getItem('auth_token');
  }

  getUserId(): number | null {
    const id = localStorage.getItem('auth_userId');
    return id ? parseInt(id, 10) : null;
  }

  getUsername(): string | null {
    return localStorage.getItem('auth_username');
  }

  getFullName(): string | null {
    return localStorage.getItem('auth_fullName');
  }

  getEmail(): string | null {
    return localStorage.getItem('auth_email');
  }

  getAvatar(): string | null {
    return localStorage.getItem('auth_avatar');
  }

  // 4. Kiểm tra xem đã đăng nhập chưa
  isLoggedIn(): boolean {
    return !!this.getToken(); 
  }

  logout(): void {
    localStorage.removeItem('auth_token');
    localStorage.removeItem('auth_userId');
    localStorage.removeItem('auth_username');
    localStorage.removeItem('auth_fullName');
    localStorage.removeItem('auth_email');
    localStorage.removeItem('auth_avatar');
    this.currentUserSubject.next({});
  }
}