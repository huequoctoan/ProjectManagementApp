package com.project.model; // Đảm bảo package khớp với cấu trúc src/main/java/com/project/model

import java.awt.Color;

/**
 * Enum định nghĩa các trạng thái của Task trong hệ thống.
 * Hỗ trợ hiển thị tên tiếng Việt và màu sắc tương ứng trên giao diện Swing.
 */
public enum TaskStatus {
    
    TODO("Chưa thực hiện", new Color(189, 195, 199)),      // Màu xám
    IN_PROGRESS("Đang làm", new Color(52, 152, 219)),     // Màu xanh dương
    REVIEW("Đang kiểm tra", new Color(241, 196, 15)),     // Màu vàng
    DONE("Hoàn thành", new Color(46, 204, 113)),          // Màu xanh lá
    CANCELLED("Đã hủy", new Color(231, 76, 60));         // Màu đỏ

    private final String displayName;
    private final Color color;

    TaskStatus(String displayName, Color color) {
        this.displayName = displayName;
        this.color = color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Color getColor() {
        return color;
    }

    /**
     * Chuyển đổi từ String (từ Database) sang Enum an toàn.
     * Tránh lỗi nếu dữ liệu trong DB không khớp.
     */
    public static TaskStatus fromString(String text) {
        for (TaskStatus status : TaskStatus.values()) {
            if (status.name().equalsIgnoreCase(text) || status.displayName.equalsIgnoreCase(text)) {
                return status;
            }
        }
        return TODO; // Mặc định là TODO nếu không tìm thấy
    }

    @Override
    public String toString() {
        return displayName;
    }
}
