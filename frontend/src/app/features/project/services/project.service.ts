import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Project } from '../models/project';
import { AuthService } from '../../auth/services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  private apiUrl = 'http://localhost:8080/api/projects';
  private memberApiUrl = 'http://localhost:8080/api/project-members';

  constructor(private http: HttpClient, private authService: AuthService) {}

  /**
   * Chỉ lấy dự án mà user hiện tại là thành viên
   */
  getAllProjects(): Observable<Project[]> {
    const userId = this.authService.getUserId();
    if (!userId) {
      throw new Error('Người dùng chưa đăng nhập');
    }
    return this.http.get<Project[]>(`${this.apiUrl}/user/${userId}`);
  }

  /**
   * Tạo dự án mới — người tạo tự động là MANAGER
   */
  createProject(project: Project): Observable<Project> {
    const userId = this.authService.getUserId();
    if (!userId) {
      throw new Error('Người dùng chưa đăng nhập');
    }
    return this.http.post<Project>(`${this.apiUrl}/user/${userId}`, project);
  }

  /**
   * Xóa dự án
   */
  deleteProject(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  /**
   * Push Model: MANAGER thêm thành viên bằng username hoặc email
   */
  addMember(projectId: number, usernameOrEmail: string): Observable<any> {
    return this.http.post(
      `${this.memberApiUrl}/project/${projectId}/add`,
      { usernameOrEmail }
    );
  }

  /**
   * Lấy danh sách thành viên của dự án
   */
  getMembersByProject(projectId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.memberApiUrl}/project/${projectId}`);
  }
}