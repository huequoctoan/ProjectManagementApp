import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogModule, MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import { Task } from '../../models/task';
import { TaskService } from '../../services/task.service';
import { ProjectService } from '../../../project/services/project.service';
import { ProjectMember } from '../../models/project-member';
import { CommentService } from '../../services/comment.service';
import { Comment } from '../../models/comment';
import { AuthService } from '../../../auth/services/auth.service';

@Component({
  selector: 'app-task-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, MatDialogModule, MatMenuModule, MatIconModule],
  templateUrl: './task-detail.component.html',
  styleUrl: './task-detail.component.scss'
})
export class TaskDetailComponent implements OnInit {
  task: Task;
  projectMembers: ProjectMember[] = [];
  comments: Comment[] = [];
  newCommentContent: string = '';
  isSaving = false;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { task: Task; projectId: number },
    private dialogRef: MatDialogRef<TaskDetailComponent>,
    private taskService: TaskService,
    private projectService: ProjectService,
    private commentService: CommentService,
    private authService: AuthService
  ) {
    this.task = { ...data.task };
    if (!this.task.assignedMembers) {
      this.task.assignedMembers = [];
    }
  }

  ngOnInit(): void {
    this.loadProjectMembers();
    this.loadComments();
  }

  loadProjectMembers(): void {
    this.projectService.getMembersByProject(this.data.projectId).subscribe({
      next: (members) => this.projectMembers = members,
      error: (err) => console.error('Lỗi tải thành viên project', err)
    });
  }

  loadComments(): void {
    if (this.task.id) {
      this.commentService.getTaskComments(this.task.id).subscribe({
        next: (comments) => this.comments = comments,
        error: (err) => console.error('Lỗi tải bình luận', err)
      });
    }
  }

  saveTask(): void {
    if (this.task.id) {
      this.isSaving = true;
      this.taskService.updateTask(this.task.id, this.task).subscribe({
        next: (updatedTask) => {
          this.isSaving = false;
          this.dialogRef.close(updatedTask);
        },
        error: (err) => {
          console.error('Lỗi lưu task', err);
          this.isSaving = false;
        }
      });
    }
  }

  addComment(): void {
    const userId = this.authService.getUserId();
    if (this.task.id && userId && this.newCommentContent.trim()) {
      this.commentService.addComment(this.task.id, userId, this.newCommentContent).subscribe({
        next: (comment) => {
          this.comments.unshift(comment);
          this.newCommentContent = '';
        },
        error: (err) => console.error('Lỗi thêm bình luận', err)
      });
    }
  }

  isMemberAssigned(memberId: number): boolean {
    return this.task.assignedMembers?.some(m => m.id === memberId) || false;
  }

  toggleMember(member: ProjectMember): void {
    if (this.isMemberAssigned(member.id)) {
      this.task.assignedMembers = this.task.assignedMembers?.filter(m => m.id !== member.id);
    } else {
      this.task.assignedMembers?.push(member);
    }
    this.saveTask();
  }

  deleteTask(): void {
    if (this.task.id && confirm('Bạn có chắc chắn muốn xóa task này?')) {
      this.taskService.deleteTask(this.task.id).subscribe({
        next: () => this.dialogRef.close({ deleted: true, id: this.task.id }),
        error: (err) => console.error('Lỗi xóa task', err)
      });
    }
  }
}

