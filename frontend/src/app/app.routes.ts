import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/components/login/login.component';
import { HomeComponent } from './features/home/components/home/home.component';
import { LayoutComponent } from './features/layout/components/layout/layout.component'; // Import Layout
import { ProjectListComponent } from './features/project/components/project-list/project-list.component';
import { TaskListComponent } from './features/task/components/task-list/task-list.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  
  // KHỐI GIAO DIỆN CHÍNH (CÓ SIDEBAR & HEADER)
  {
    path: '',
    component: LayoutComponent, 
    children: [                 
      { path: 'home', component: HomeComponent },
      { path: 'projects', component: ProjectListComponent },
      { path: 'tasks', component: TaskListComponent }
    ]
  }
];