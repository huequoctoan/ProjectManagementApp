import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router'; 

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet], // Khai báo sử dụng RouterOutlet
  template: `<router-outlet></router-outlet>`, // Trả lại cái khung hình trống
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'task-frontend';
}