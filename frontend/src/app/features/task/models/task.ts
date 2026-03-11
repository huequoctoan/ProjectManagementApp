export interface Task {
  id?: number;
  title?: string;       // Đổi name thành title cho khớp Backend
  description?: string; // Khớp với Backend
  status?: string;      // "TODO", "IN_PROGRESS", "DONE"
  
  // Có thể khai báo sẵn để sau này dùng, không bắt buộc điền ngay
  project?: any; 
  assignee?: any; 
}