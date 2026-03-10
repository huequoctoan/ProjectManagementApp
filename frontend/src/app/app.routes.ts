import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/components/login/login.component';
import { HomeComponent } from './features/home/components/home/home.component';

export const routes: Routes = [
  // Nếu người dùng vào đường dẫn trống, tự động đẩy sang trang login
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  
  // Đường dẫn /login sẽ mở LoginComponent
  { path: 'login', component: LoginComponent },
  
  // Đường dẫn /home sẽ mở HomeComponent
  { path: 'home', component: HomeComponent }
];