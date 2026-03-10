import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { AuthRequest } from '../../models/auth.model';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router'; 

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

  
  constructor(private authService: AuthService, private router: Router) {}

  onLogin(): void {
    this.errorMessage = '';

    this.authService.login(this.request).subscribe({
      next: (response) => {
        if (response.authenticate && response.token) {
          this.authService.setToken(response.token);
          

          this.router.navigate(['/home']); 
          
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