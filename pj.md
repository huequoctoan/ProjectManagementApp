Kiến trúc phần mềm:
Mô hình đa tầng (Multi-layered): UI – Business Logic – Data Access
.
Kiến trúc Core-Plugin: Sử dụng Java Reflection và URLClassLoader để nạp các module chức năng dưới dạng file .jar tại thời điểm chạy (runtime)
.
Thiết kế: Low coupling (liên kết lỏng lẻo) và High cohesion (độ tập trung cao)
.
Mẫu thiết kế (Design Patterns) bắt buộc: Observer, Strategy, Factory, và Template Method
.
Xử lý đa luồng (Concurrency): Sử dụng SwingWorker và Timer để cập nhật dữ liệu nền, tránh treo giao diện
.
Kiểm thử: Unit Test sử dụng JUnit cho các lớp nghiệp vụ chính
.
3. Các thực thể chính (Domain Model)
Hệ thống xoay quanh 3 thực thể cốt lõi
:
User: id, username, password, role (Admin, Manager, Member)
.
Project: MaDuAn, TenDuAn, NgayBatDau, NgayKetThuc
.
Task: MaCongViec, TenCongViec, Deadline, TrangThai (To Do, In Progress, Done, Cancel)
.
4. Phân quyền người dùng (User Roles)
Admin: Quản trị hệ thống, phê duyệt đăng ký, phân quyền vai trò
.
Manager: Tạo/sửa/xóa dự án, tạo Task, gán người thực hiện, theo dõi báo cáo tiến độ
.
Member: Tiếp nhận Task, cập nhật trạng thái công việc, thảo luận/bình luận
.
5. Danh sách chức năng (Functional Requirements)
Quản lý Dự án (Project Plugin): Tạo, chỉnh sửa, xóa và phân quyền quản lý dự án
.
Quản lý Công việc (Task Plugin): Tạo Task, gán Assignee, thiết lập Deadline, cập nhật trạng thái
.
Quản lý Người dùng (User Plugin): Đăng ký, đăng nhập (xác thực JWT), phân vai trò
.
Cộng tác & Thông báo: Bình luận trực tiếp trong Task, gửi thông báo khi thay đổi trạng thái hoặc sắp đến deadline
.
Thống kê & Báo cáo (Reporting Plugin): Vẽ biểu đồ tỉ lệ hoàn thành công việc và phân bổ Task theo mức độ ưu tiên
.
7. Ràng buộc thiết kế (Design Constraints)
Các Plugin không được gọi trực tiếp lẫn nhau mà phải giao tiếp thông qua Core hoặc các Interface chung (ví dụ: IPlugin, ITaskService)
.
Mọi tác vụ truy xuất dữ liệu nặng bắt buộc phải chạy trên SwingWorker
.
Dữ liệu được quản lý qua mẫu DAO (Data Access Object) để tách biệt logic nghiệp vụ với nguồn lưu trữ (MySQL hoặc JSON)
.
