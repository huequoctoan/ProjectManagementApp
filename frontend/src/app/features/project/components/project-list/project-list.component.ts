import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProjectService } from '../../services/project.service';
import { Project } from '../../models/project';

@Component({
  selector: 'app-project-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './project-list.component.html',
  styleUrl: './project-list.component.scss'
})
export class ProjectListComponent implements OnInit {
  projects: Project[] = [];

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