import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  user = {
    username: '',
    password: '',
    email: '',
    fullName: '',
    role: 'MEMBER' // Default role
  };
  errorMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  onRegister(): void {
    this.errorMessage = '';
    
    if (!this.user.username || !this.user.password || !this.user.email) {
        this.errorMessage = 'Vui lòng điền đầy đủ thông tin bắt buộc.';
        return;
    }

    this.authService.register(this.user).subscribe({
      next: (response) => {
        if (response.authenticate && response.token) {
          this.authService.setToken(response.token);
          if (response.userId) this.authService.setUserId(response.userId);
          if (response.username) this.authService.setUsername(response.username);
          if (response.fullName) this.authService.setFullName(response.fullName);
          if (response.email) this.authService.setEmail(response.email);
          if (response.role) this.authService.setRole(response.role);
          
          this.router.navigate(['/projects']);
        } else {
          this.errorMessage = 'Đăng ký thất bại, vui lòng thử lại.';
        }
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Có lỗi xảy ra khi đăng ký username này.';
        console.error(err);
      }
    });
  }
}
