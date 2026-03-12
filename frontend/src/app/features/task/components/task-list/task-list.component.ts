import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TaskService } from '../../services/task.service';
import { ProjectService } from '../../../project/services/project.service';
import { Task } from '../../models/task';
import { Project } from '../../../project/models/project';

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './task-list.component.html',
  styleUrl: './task-list.component.scss'
})
export class TaskListComponent implements OnInit {
  tasks: Task[] = [];
  projects: Project[] = []; // Danh sách dự án để chọn
  loading: boolean = false;
  showModal: boolean = false;
  newTask: Task = { title: '', description: '', status: 'TODO', project: null };

  constructor(
    private taskService: TaskService,
    private projectService: ProjectService
  ) {}

  ngOnInit(): void {
    this.loadTasks();
    this.loadProjects();
  }

  loadTasks(): void {
    this.loading = true;
    this.taskService.getAllTasks().subscribe({
      next: (data) => {
        this.tasks = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Lỗi tải danh sách công việc:', err);
        this.loading = false;
      }
    });
  }

  loadProjects(): void {
    this.projectService.getAllProjects().subscribe({
      next: (data) => {
        this.projects = data;
      },
      error: (err) => console.error('Lỗi tải danh sách dự án:', err)
    });
  }

  openModal(): void {
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
    this.newTask = { title: '', description: '', status: 'TODO', project: null };
  }

  onSubmit(): void {
    if (this.newTask.title) {
      this.taskService.createTask(this.newTask).subscribe({
        next: (res) => {
          alert('Thêm công việc thành công!');
          this.loadTasks();
          this.closeModal();
        },
        error: (err) => console.error('Lỗi khi tạo công việc:', err)
      });
    }
  }

  onDelete(id: number | undefined): void {
    if (id && confirm('Bạn có chắc chắn muốn xóa công việc này?')) {
      this.taskService.deleteTask(id).subscribe({
        next: () => {
          alert('Xóa thành công!');
          this.loadTasks();
        },
        error: (err) => console.error('Lỗi khi xóa công việc:', err)
      });
    }
  }

  getStatusClass(status: string | undefined): string {
    if (!status) return '';
    return `status-${status.toLowerCase()}`;
  }
}
