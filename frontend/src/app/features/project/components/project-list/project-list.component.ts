import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ProjectService } from '../../services/project.service';
import { Project } from '../../models/project';

@Component({
  selector: 'app-project-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './project-list.component.html',
  styleUrl: './project-list.component.scss'
})
export class ProjectListComponent implements OnInit {
  projects: Project[] = [];

  // Create Project State
  showCreateModal = false;
  newProject: Project = { name: '', description: '' };

  // Add Member State
  showAddMemberModal = false;
  selectedProjectId: number | null = null;
  newMemberUsernameOrEmail = '';

  constructor(private projectService: ProjectService) {}

  ngOnInit(): void {
    this.loadProjects();
  }

  loadProjects(): void {
    this.projectService.getAllProjects().subscribe({
      next: (data) => this.projects = data,
      error: (err) => console.error('Lỗi tải dự án', err)
    });
  }

  onDelete(id: number | undefined): void {
    if (id && confirm('Bạn có chắc chắn muốn xóa dự án này?')) {
      this.projectService.deleteProject(id).subscribe({
        next: () => {
          this.loadProjects();
        },
        error: (err) => alert('Lỗi: Không được phép xóa hoặc dự án đang có dữ liệu ràng buộc.')
      });
    }
  }

  // ===== CREATE PROJECT =====
  openCreateModal(): void {
    this.newProject = { name: '', description: '' };
    this.showCreateModal = true;
  }

  closeCreateModal(): void {
    this.showCreateModal = false;
  }

  submitCreateProject(): void {
    if (!this.newProject.name?.trim()) {
      alert('Vui lòng nhập tên dự án!');
      return;
    }
    
    this.projectService.createProject(this.newProject).subscribe({
      next: () => {
        this.closeCreateModal();
        this.loadProjects();
      },
      error: (err) => alert('Lỗi khi tạo dự án: ' + err.message)
    });
  }

  // ===== ADD MEMBER =====
  openAddMemberModal(projectId: number | undefined): void {
    if (!projectId) return;
    this.selectedProjectId = projectId;
    this.newMemberUsernameOrEmail = '';
    this.showAddMemberModal = true;
  }

  closeAddMemberModal(): void {
    this.showAddMemberModal = false;
    this.selectedProjectId = null;
  }

  submitAddMember(): void {
    if (!this.selectedProjectId) return;
    if (!this.newMemberUsernameOrEmail.trim()) {
      alert('Vui lòng nhập Username hoặc Email');
      return;
    }

    this.projectService.addMember(this.selectedProjectId, this.newMemberUsernameOrEmail).subscribe({
      next: () => {
        alert('Đã thêm thành viên thành công!');
        this.closeAddMemberModal();
      },
      error: (err) => {
        // Lấy error message từ backend nếu có (Conflict 409 hoặc Not Found 404)
        const msg = err.error?.message || err.message || 'Lỗi khi thêm thành viên';
        alert('Thất bại: ' + msg);
      }
    });
  }
}