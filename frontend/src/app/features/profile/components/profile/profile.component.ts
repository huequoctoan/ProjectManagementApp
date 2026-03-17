import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../auth/services/auth.service';
import { UserService } from '../../../user/services/user.service';
import { User } from '../../../user/models/user.model';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent implements OnInit {
  userId: number | null = null;
  user: Partial<User> = {};
  isLoading = false;
  successMessage = '';

  constructor(
    private authService: AuthService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.userId = this.authService.getUserId();
    if (this.userId) {
      this.loadUser();
    }
  }

  loadUser(): void {
    if(!this.userId) return;
    this.isLoading = true;
    this.userService.getUserById(this.userId).subscribe({
      next: (data) => {
        this.user = data;
        // Mật khẩu không nên hiển thị, reset nó
        this.user.password = ''; 
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Lỗi tải user', err);
        this.isLoading = false;
      }
    });
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.user.avatar = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  }

  saveProfile(): void {
    if(!this.userId) return;
    this.isLoading = true;
    this.successMessage = '';
    
    this.userService.updateUser(this.userId, this.user).subscribe({
      next: () => {
        this.isLoading = false;
        this.successMessage = 'Cập nhật hồ sơ thành công!';
        
        // Sync to Auth Service for Topbar (this triggers BehaviorSubject)
        if(this.user.fullName) this.authService.setFullName(this.user.fullName);
        if(this.user.email) this.authService.setEmail(this.user.email);
        if(this.user.avatar) this.authService.setAvatar(this.user.avatar);
        
        // Timeout to clear message
        setTimeout(() => {
          this.successMessage = '';
        }, 3000);
      },
      error: (err) => {
        console.error('Lỗi cập nhật', err);
        this.isLoading = false;
      }
    });
  }
}
