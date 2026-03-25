import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface BoardColumn {
  id?: number;
  name: string;
  color?: string;
  position?: number;
}

@Injectable({
  providedIn: 'root'
})
export class ColumnService {
  private apiUrl = 'http://localhost:8080/api/columns';

  constructor(private http: HttpClient) {}

  getColumnsByProject(projectId: number): Observable<BoardColumn[]> {
    return this.http.get<BoardColumn[]>(`${this.apiUrl}/project/${projectId}`);
  }

  createColumn(projectId: number, column: BoardColumn): Observable<BoardColumn> {
    return this.http.post<BoardColumn>(`${this.apiUrl}/project/${projectId}`, column);
  }

  updateColumn(columnId: number, column: Partial<BoardColumn>): Observable<BoardColumn> {
    return this.http.put<BoardColumn>(`${this.apiUrl}/${columnId}`, column);
  }
}
