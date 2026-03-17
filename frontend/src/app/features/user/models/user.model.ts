export interface User {
  id: number;
  username: string;
  email: string;
  password?: string;
  fullName?: string;
  dateOfBirth?: string;
  phoneNumber?: string;
  avatar?: string;
}
