export interface AuthRequest {
  username?: string;
  password?: string;
}

export interface AuthResponse {
  token?: string; 
  authenticate?: boolean;
  userId?: number;
  username?: string;
  fullName?: string;
  email?: string;
  avatar?: string;
}