import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/components/login/login.component';
import { HomeComponent } from './features/home/components/home/home.component';
import { LayoutComponent } from './features/layout/components/layout/layout.component'; // Import Layout

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  
  // KHỐI GIAO DIỆN CHÍNH (CÓ SIDEBAR & HEADER)
  {
    path: '',
    component: LayoutComponent, 
    children: [                 
      { path: 'home', component: HomeComponent },
      
    ]
  }
];