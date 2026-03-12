import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; // Thêm FormsModule
import { ProjectService } from '../../services/project.service';
import { Project } from '../../models/project';

@Component({
  selector: 'app-project-list',
  standalone: true,
  imports: [CommonModule, FormsModule], // Thêm FormsModule vào đây
  templateUrl: './project-list.component.html',
  styleUrl: './project-list.component.scss'
})
export class ProjectListComponent implements OnInit {
  projects: Project[] = [];
  showModal: boolean = false; // Trạng thái ẩn/hiện modal
  newProject: Project = { name: '', description: '' }; // Model cho dự án mới

  constructor(private projectService: ProjectService) {}

  ngOnInit(): void {
    this.loadProjects();
  }

  loadProjects(): void {
    this.projectService.getAllProjects().subscribe({
      next: (data) => {
        this.projects = data;
      },
      error: (err) => console.error('Lỗi tải dự án', err)
    });
  }

  openModal(): void {
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
    this.newProject = { name: '', description: '' }; // Reset form
  }

  onSubmit(): void {
    if (this.newProject.name) {
      this.projectService.createProject(this.newProject).subscribe({
        next: (res) => {
          alert('Thêm dự án thành công!');
          this.loadProjects(); // Tải lại danh sách
          this.closeModal();   // Đóng modal
        },
        error: (err) => console.error('Lỗi khi tạo dự án', err)
      });
    }
  }

  onDelete(id: number | undefined): void {
    if (id && confirm('Bạn có chắc chắn muốn xóa dự án này?')) {
      this.projectService.deleteProject(id).subscribe({
        next: () => {
          alert('Xóa thành công!');
          this.loadProjects(); // Tải lại bảng sau khi xóa
        },
        error: (err) => console.error('Lỗi khi xóa', err)
      });
    }
  }
}