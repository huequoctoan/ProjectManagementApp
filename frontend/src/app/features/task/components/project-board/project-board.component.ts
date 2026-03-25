import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { DragDropModule, CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { TaskService } from '../../services/task.service';
import { ColumnService, BoardColumn } from '../../../project/services/column.service';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { TaskDetailComponent } from '../task-detail/task-detail.component';
import { Task } from '../../models/task';
import { ProjectMember } from '../../models/project-member';

interface TaskCard {
  id?: number;
  title: string;
  description?: string;
  status: string;
  priority: 'HIGH' | 'MEDIUM' | 'LOW';
  dueDate?: string;
  assignedMembers: ProjectMember[];
}

interface KanbanColumn {
  id: string; // String representation of BoardColumn.id
  label: string;
  color: string;
  tasks: TaskCard[];
}

@Component({
  selector: 'app-project-board',
  standalone: true,
  imports: [CommonModule, FormsModule, DragDropModule, MatDialogModule, RouterModule],
  templateUrl: './project-board.component.html',
  styleUrl: './project-board.component.scss'
})
export class ProjectBoardComponent implements OnInit {
  projectId!: number;
  columns: KanbanColumn[] = [];
  showAddListInput = false;
  newListName = '';
  cardDrafts: { [columnId: string]: string } = {};
  isSaving = false;
  totalTasks = 0;
  completionRate = 0;

  constructor(
    private route: ActivatedRoute,
    private taskService: TaskService,
    private columnService: ColumnService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.projectId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadBoard();
  }

  loadBoard(): void {
    this.columnService.getColumnsByProject(this.projectId).subscribe({
      next: (cols: BoardColumn[]) => {
        this.columns = cols.map((c: BoardColumn) => ({
          id: c.id!.toString(),
          label: c.name,
          color: c.color || '#F1F2F4',
          tasks: []
        }));
        
        this.taskService.getTasksByProjectId(this.projectId).subscribe({
          next: (tasks: any[]) => {
            this.totalTasks = tasks.length;
            let doneCount = 0;
            
            tasks.forEach(task => {
              const col = this.columns.find(c => c.id === task.status);
              if (col) {
                col.tasks.push({
                  id: task.id,
                  title: task.title || '',
                  description: task.description,
                  status: task.status,
                  priority: task.priority || 'MEDIUM',
                  dueDate: task.dueDate,
                  assignedMembers: task.assignedMembers || []
                });
                if (task.status?.toUpperCase() === 'DONE') doneCount++;
              }
            });
            
            this.completionRate = this.totalTasks > 0 ? Math.round((doneCount / this.totalTasks) * 100) : 0;
          }
        });
      }
    });
  }

  getPriorityLabel(priority: string): string {
    const map: Record<string, string> = { HIGH: 'Cao', MEDIUM: 'Trung bình', LOW: 'Thấp' };
    return map[priority] || priority;
  }

  drop(event: CdkDragDrop<TaskCard[]>, columnId: string) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(event.previousContainer.data, event.container.data, event.previousIndex, event.currentIndex);
      const movedTask = event.container.data[event.currentIndex];
      if (movedTask.id) {
        this.isSaving = true;
        this.taskService.updateTask(movedTask.id, { status: columnId }).subscribe({
          next: () => { this.isSaving = false; },
          error: () => { this.isSaving = false; }
        });
      }
    }
  }

  isOverdue(dueDate?: string): boolean {
    if (!dueDate) return false;
    const d = new Date(dueDate);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    return d < today;
  }

  get connectedToIds(): string[] {
    return this.columns.map(c => c.id);
  }

  openAddList() { this.showAddListInput = true; this.newListName = ''; }
  cancelAddList() { this.showAddListInput = false; }
  submitAddList() {
    if (!this.newListName.trim()) return;
    this.columnService.createColumn(this.projectId, { name: this.newListName.trim() }).subscribe(res => {
      this.columns.push({ id: res.id!.toString(), label: res.name, color: '#F1F2F4', tasks: [] });
      this.showAddListInput = false;
    });
  }

  openAddCard(columnId: string) { this.cardDrafts[columnId] = ''; }
  cancelAddCard(columnId: string) { delete this.cardDrafts[columnId]; }
  submitAddCard(columnId: string) {
    const title = this.cardDrafts[columnId];
    if (!title?.trim()) return;
    this.taskService.createTask({ title: title.trim(), status: columnId, project: { id: this.projectId } } as Task).subscribe(res => {
      const col = this.columns.find(c => c.id === columnId);
      if (col) col.tasks.push({ 
        id: res.id, 
        title: res.title || '', 
        status: columnId, 
        priority: 'MEDIUM', 
        assignedMembers: [] 
      });
      delete this.cardDrafts[columnId];
    });
  }

  openTaskDetail(taskCard: TaskCard): void {
    const dialogRef = this.dialog.open(TaskDetailComponent, {
      width: '768px',
      maxWidth: '95vw',
      data: { task: taskCard, projectId: this.projectId },
      autoFocus: false,
      panelClass: 'trello-task-dialog'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (result.deleted) {
          const col = this.columns.find(c => c.id === taskCard.status);
          if (col) col.tasks = col.tasks.filter(t => t.id !== result.id);
        } else {
          const col = this.columns.find(c => c.id === taskCard.status);
          if (col) {
            const idx = col.tasks.findIndex(t => t.id === result.id);
            if (idx !== -1) col.tasks[idx] = { ...col.tasks[idx], ...result };
          }
        }
      }
    });
  }

  deleteTask(taskId: number, columnId: string, event: Event) {
    event.stopPropagation();
    if (confirm('Xóa công việc?')) {
      this.taskService.deleteTask(taskId).subscribe(() => {
        const col = this.columns.find(c => c.id === columnId);
        if (col) col.tasks = col.tasks.filter(t => t.id !== taskId);
      });
    }
  }
}


