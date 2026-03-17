import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { AuthRequest } from '../../models/auth.model';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router'; 

import { ProjectService } from '../../../project/services/project.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  request: AuthRequest = { username: '', password: '' };
  errorMessage: string = '';

  constructor(
    private authService: AuthService, 
    private router: Router,
    private projectService: ProjectService
  ) {}

  onLogin(): void {
    this.errorMessage = '';

    this.authService.login(this.request).subscribe({
      next: (response) => {
        if (response.authenticate && response.token) {
          this.authService.setToken(response.token);
          if (response.userId) {
            this.authService.setUserId(response.userId);
            if (response.username) {
              this.authService.setUsername(response.username);
            }
            if (response.fullName) {
              this.authService.setFullName(response.fullName);
            }
            if (response.email) {
              this.authService.setEmail(response.email);
            }
            if (response.avatar) {
              this.authService.setAvatar(response.avatar);
            }
            
            // Lấy danh sách dự án ngay sau khi đăng nhập
            this.projectService.getAllProjects().subscribe({
              next: (projects) => {
                if (projects && projects.length > 0) {
                  // Chuyển thẳng đến project đầu tiên
                  this.router.navigate(['/projects', projects[0].id, 'board']);
                } else {
                  // Nếu chưa có dự án nào thì ra màn hình danh sách dự án
                  this.router.navigate(['/projects']);
                }
              },
              error: () => this.router.navigate(['/projects'])
            });
          } else {
            this.router.navigate(['/projects']);
          }
          
        } else {
          this.errorMessage = 'Đăng nhập thất bại, vui lòng thử lại.';
        }
      },
      error: (err) => {
        this.errorMessage = 'Sai username hoặc password!';
        console.error(err);
      }
    });
  }
}