import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, RouterLinkActive, Router } from '@angular/router';
import { AuthService } from '../../../auth/services/auth.service';
import { ProjectService } from '../../../project/services/project.service';
import { Project } from '../../../project/models/project';
import { NotificationService } from '../../../notification/services/notification.service';
import { AppNotification } from '../../../notification/models/notification.model';

@Component({
  selector: 'app-layout',
  standalone: true,
  
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive], 
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss'
})
export class LayoutComponent implements OnInit {
  
  showDropdown: boolean = false;
  showNotifDropdown: boolean = false;
  projects: Project[] = [];
  notifications: AppNotification[] = [];
  
  // User Profile
  username: string = '';
  fullName: string = '';
  email: string = '';
  avatar: string | null = null;

  constructor(
    private authService: AuthService, 
    private router: Router,
    private projectService: ProjectService,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.loadProjects();
    this.loadNotifications();
    
    // Subscribe to real-time User info changes
    this.authService.currentUser$.subscribe(user => {
      this.username = user.username || '';
      this.fullName = user.fullName || '';
      this.email = user.email || '';
      this.avatar = user.avatar || null;
    });
  }

  get unreadCount(): number {
    return this.notifications.filter(n => !n.read).length;
  }

  loadNotifications(): void {
    const userId = this.authService.getUserId();
    if (userId) {
      this.notificationService.getUserNotifications(userId).subscribe({
        next: (data) => this.notifications = data,
        error: (err) => console.error('Lỗi tải thông báo', err)
      });
    }
  }

  toggleNotifDropdown(): void {
    this.showNotifDropdown = !this.showNotifDropdown;
    if (this.showNotifDropdown) {
      this.showDropdown = false; // Đóng dropdown kia
    }
  }

  onNotificationClick(noti: AppNotification): void {
    const userId = this.authService.getUserId();
    if (!userId) return;

    if (!noti.read) {
      this.notificationService.markAsRead(noti.id, userId).subscribe(() => {
        noti.read = true; // Update local state
        this.navigateToProject(noti.project?.id);
      });
    } else {
      this.navigateToProject(noti.project?.id);
    }
  }
  
  markAllAsRead(): void {
    const userId = this.authService.getUserId();
    if (!userId) return;
    
    this.notificationService.markAllAsRead(userId).subscribe(() => {
      this.notifications.forEach(n => n.read = true);
    });
  }

  private navigateToProject(projectId: number | undefined): void {
    if (projectId) {
      this.showNotifDropdown = false;
      this.router.navigate(['/projects', projectId, 'board']);
    }
  }

  loadProjects(): void {
    const userId = this.authService.getUserId();
    if (userId) {
      this.projectService.getAllProjects().subscribe({
        next: (data) => this.projects = data,
        error: (err) => console.error('Lỗi tải danh sách dự án cho sidebar', err)
      });
    }
  }

  toggleDropdown(): void {
    this.showDropdown = !this.showDropdown;
    if (this.showDropdown) {
      this.showNotifDropdown = false;
    }
  }

  
  onLogout(): void {
    this.authService.logout(); 
    this.router.navigate(['/login']); 
  }
}