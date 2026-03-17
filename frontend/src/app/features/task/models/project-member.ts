import { User } from '../../user/models/user.model';
import { Project } from '../../project/models/project';

export interface ProjectMember {
  id: number;
  role: 'MANAGER' | 'MEMBER';
  user: User;
  project: Project;
}
