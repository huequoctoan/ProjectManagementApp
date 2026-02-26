package com.project.core.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Lớp đại diện cho một Công việc (Task) trong hệ thống.
 * Áp dụng nguyên tắc Encapsulation (Đóng gói) và Rich Domain Model.
 */
public class Task {
    // 1. Dùng 'final' cho khóa chính (ID) để đảm bảo tính bất biến (Immutability)
    private final String id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDate deadline;

    // 2. Constructor đầy đủ tham số
    public Task(String id, String title, String description, TaskStatus status, LocalDate deadline) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Task ID cannot be null or empty");
        }
        this.id = id;
        this.setTitle(title); // Tái sử dụng logic kiểm tra (validation) ở setter
        this.description = description;
        this.status = (status != null) ? status : TaskStatus.TODO;
        this.deadline = (deadline != null) ? deadline : LocalDate.now().plusDays(7);
    }

    // 3. Constructor rút gọn (Overloading) giúp tạo nhanh một Task mới
    public Task(String id, String title) {
        this(id, title, "", TaskStatus.TODO, null);
    }

    // --- Getters & Setters kèm Validation ---

    public String getId() {
        return id; // Không có setId() vì id đã là final, bảo vệ tính toàn vẹn dữ liệu
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        // 4. Kiểm tra dữ liệu đầu vào (Validation)
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be empty");
        }
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Task status cannot be null");
        }
        this.status = status;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        // Ràng buộc nghiệp vụ: Hạn chót không được ở trong quá khứ
        if (deadline != null && deadline.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Deadline cannot be in the past");
        }
        this.deadline = deadline;
    }

    // --- Business Logic Methods (Rich Domain Model) ---

    // 5. Tích hợp logic nghiệp vụ trực tiếp vào thực thể
    public boolean isOverdue() {
        return LocalDate.now().isAfter(this.deadline) && this.status != TaskStatus.DONE;
    }

    // --- Standard Methods ---

    // 6. Ghi đè equals() và hashCode() để so sánh các Task chuẩn xác
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id.equals(task.id); // Hai Task được coi là giống nhau nếu có cùng ID
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // 7. Ghi đè toString() giúp in ra log dễ đọc khi debug
    @Override
    public String toString() {
        return String.format("Task[id='%s', title='%s', status=%s, deadline=%s]",
                id, title, status, deadline);
    }

    public Task(String id, String title, TaskStatus status) {
        this(id, title, "", status, null);
    }
}