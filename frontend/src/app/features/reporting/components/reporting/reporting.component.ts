import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { TaskService } from '../../../task/services/task.service';
import { Task } from '../../../task/models/task';

@Component({
  selector: 'app-reporting',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './reporting.component.html',
  styleUrl: './reporting.component.scss'
})
export class ReportingComponent implements OnInit {
  projectId!: number;
  tasks: Task[] = [];
  statusCounts: { [key: string]: number } = {};
  priorityCounts: { [key: string]: number } = {};
  totalTasks = 0;
  completionRate = 0;

  constructor(
    private route: ActivatedRoute,
    private taskService: TaskService
  ) {}

  ngOnInit(): void {
    this.projectId = Number(this.route.parent?.snapshot.paramMap.get('id'));
    this.loadStats();
  }

  loadStats(): void {
    this.taskService.getTasksByProjectId(this.projectId).subscribe({
      next: (tasks) => {
        this.tasks = tasks;
        this.totalTasks = tasks.length;
        this.calculateStats();
      }
    });
  }

  calculateStats(): void {
    this.statusCounts = {};
    this.priorityCounts = {};
    let completed = 0;

    this.tasks.forEach(t => {
      // Status
      const status = t.status || 'UNKNOWN';
      this.statusCounts[status] = (this.statusCounts[status] || 0) + 1;
      if (status.toUpperCase() === 'DONE' || status.toUpperCase() === 'COMPLETED') {
        completed++;
      }

      // Priority
      const prio = t.priority || 'MEDIUM';
      this.priorityCounts[prio] = (this.priorityCounts[prio] || 0) + 1;
    });

    this.completionRate = this.totalTasks > 0 ? Math.round((completed / this.totalTasks) * 100) : 0;
  }

  getStatusKeys(): string[] {
    return Object.keys(this.statusCounts);
  }

  getPriorityKeys(): string[] {
    return Object.keys(this.priorityCounts);
  }

  getPercentage(count: number): number {
    return this.totalTasks > 0 ? Math.round((count / this.totalTasks) * 100) : 0;
  }
}
