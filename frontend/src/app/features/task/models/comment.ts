import { User } from '../../user/models/user.model';
import { Task } from './task';

export interface Comment {
  id?: number;
  content: string;
  createdAt: string;
  user: User;
  task?: Task;
}
