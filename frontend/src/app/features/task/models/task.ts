import { Project } from '../../project/models/project';
import { ProjectMember } from './project-member';

export interface Task {
  id?: number;
  title?: string;
  description?: string;
  status?: string;
  dueDate?: string;
  project?: Project;
  assignedMembers?: ProjectMember[];
}