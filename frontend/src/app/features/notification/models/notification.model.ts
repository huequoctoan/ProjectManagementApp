export interface UserInfo {
  id: number;
  username: string;
  email: string;
}

export interface ProjectInfo {
  id: number;
  name: string;
}

export interface AppNotification {
  id: number;
  user: UserInfo;
  project: ProjectInfo;
  message: string;
  read: boolean;
  createdAt: string;
}
