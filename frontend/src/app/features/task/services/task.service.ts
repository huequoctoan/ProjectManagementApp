import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';   // thêm
import { Observable } from 'rxjs';
import { Task } from '../models/task';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  private apiUrl = 'http://localhost:8080/api/tasks';   

  constructor(private http: HttpClient) { }   //

  getAllTasks(): Observable<Task[]> {
    return this.http.get<Task[]>(this.apiUrl);
  }

}