import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AppNotification } from '../models/notification.model';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private apiUrl = 'http://localhost:8080/api/notifications';

  constructor(private http: HttpClient) { }

  getUserNotifications(userId: number): Observable<AppNotification[]> {
    return this.http.get<AppNotification[]>(`${this.apiUrl}/user/${userId}`);
  }

  markAsRead(id: number, userId: number): Observable<AppNotification> {
    return this.http.put<AppNotification>(`${this.apiUrl}/${id}/read/user/${userId}`, {});
  }
  
  markAllAsRead(userId: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/user/${userId}/read-all`, {});
  }
}
