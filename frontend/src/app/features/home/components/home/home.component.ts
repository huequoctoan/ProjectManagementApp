import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ProjectService } from '../../../project/services/project.service';
import { Project } from '../../../project/models/project';
import { TaskService } from '../../../task/services/task.service';
import { Task } from '../../../task/models/task';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit {
  
  taskSummary = { todo: 0, inProgress: 0, done: 0, cancelled: 0 };
  
  teamMembers = [
    { name: 'Nguyễn A', avatar: 'https://i.pravatar.cc/150?img=11' },
    { name: 'Trần B', avatar: 'https://i.pravatar.cc/150?img=12' }
  ];

  realProjects: Project[] = [];

  filteredProjects: Project[] = []; 
  searchTerm: string = '';

 
  constructor(
    private projectService: ProjectService,
    private taskService: TaskService
  ) { }

  ngOnInit(): void {
    this.fetchRealProjects();
    this.fetchRealTasks(); // Gọi hàm đếm task ngay khi mở trang
  }

  fetchRealProjects(): void {
    this.projectService.getAllProjects().subscribe({
      next: (data) => {
        this.realProjects = data;
        this.filteredProjects = data; 
      },
      error: (err) => console.error('Lỗi tải dự án', err)
    });
  }

  onSearch(): void {
    if (!this.searchTerm) {
      // Nếu ô tìm kiếm trống, trả lại danh sách ban đầu
      this.filteredProjects = this.realProjects;
    } else {
      // Nếu có chữ, lọc ra những dự án có tên chứa chữ đó (không phân biệt hoa/thường)
      this.filteredProjects = this.realProjects.filter(p => 
        p.name?.toLowerCase().includes(this.searchTerm.toLowerCase())
      );
    }
  }

  fetchRealTasks(): void {
    this.taskService.getAllTasks().subscribe({
      next: (tasks: any[]) => {
        this.taskSummary = { todo: 0, inProgress: 0, done: 0, cancelled: 0 };

        tasks.forEach(task => {
          if (task.status === 'TODO') {
            this.taskSummary.todo++;
          } else if (task.status === 'IN_PROGRESS') {
            this.taskSummary.inProgress++;
          } else if (task.status === 'DONE') {
            this.taskSummary.done++;
          } else if (task.status === 'CANCELLED') {
            this.taskSummary.cancelled++;
          }
        });
        
        console.log('Thống kê công việc hoàn tất:', this.taskSummary);
      },
      error: (err: any[]) => console.error('Lỗi tải công việc', err)
    });
  }

  getPercent(value: number): number {
    const total = this.taskSummary.todo + this.taskSummary.inProgress + this.taskSummary.done + this.taskSummary.cancelled;
    if (total === 0) return 0;
    return Math.round((value / total) * 100);
  }
}