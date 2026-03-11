import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, RouterLinkActive, Router } from '@angular/router';
import { AuthService } from '../../../auth/services/auth.service';

@Component({
  selector: 'app-layout',
  standalone: true,
  
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive], 
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss'
})
export class LayoutComponent {
  
  showDropdown: boolean = false;

  constructor(private authService: AuthService, private router: Router) {}

  
  toggleDropdown(): void {
    this.showDropdown = !this.showDropdown;
  }

  
  onLogout(): void {
    this.authService.logout(); 
    this.router.navigate(['/login']); 
  }
}