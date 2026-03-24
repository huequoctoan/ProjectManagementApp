import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/components/login/login.component';
import { RegisterComponent } from './features/auth/components/register/register.component';
import { LayoutComponent } from './features/layout/components/layout/layout.component';
import { ProjectListComponent } from './features/project/components/project-list/project-list.component';
import { ProjectBoardComponent } from './features/task/components/project-board/project-board.component';
import { ProfileComponent } from './features/profile/components/profile/profile.component';
import { ReportingComponent } from './features/reporting/components/reporting/reporting.component';
import { RouterModule } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  
  // KHỐI GIAO DIỆN CHÍNH (CÓ SIDEBAR & HEADER)
  {
    path: '',
    component: LayoutComponent, 
    children: [                 
      { path: 'projects', component: ProjectListComponent },
      { path: 'projects/:id/board', component: ProjectBoardComponent },
      { path: 'projects/:id/reporting', component: ReportingComponent },
      { path: 'profile', component: ProfileComponent },
    ]
  }
];