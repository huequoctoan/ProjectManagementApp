    package com.project.core.model;

    /**
     * Enum định nghĩa các vai trò người dùng trong hệ thống.
     * Gắn liền với logic phân quyền truy cập chức năng (Plugin access control).
     */
    public enum UserRole {
        
        // Admin: Toàn quyền hệ thống, quản lý User và nạp Plugin
        ADMIN(3, "Quản trị viên"),
        
        // Manager: Quản lý dự án, phân công Task, xem báo cáo
        MANAGER(2, "Quản lý"),
        
        // Member: Thực hiện task, cập nhật trạng thái công việc
        MEMBER(1, "Thành viên"),
        
        // Guest: Chỉ xem thông tin công khai (nếu có)
        GUEST(0, "Khách");

        private final int level;
        private final String description;

        UserRole(int level, String description) {
            this.level = level;
            this.description = description;
        }

        public int getLevel() {
            return level;
        }

        public String getDescription() {
            return description;
        }

        /**
         * Kiểm tra xem vai trò hiện tại có quyền cao hơn hoặc bằng vai trò yêu cầu không.
         * Dùng để check quyền mở các Plugin nhạy cảm.
         */
        public boolean hasPermission(UserRole requiredRole) {
            return this.level >= requiredRole.getLevel();
        }

        /**
         * Chuyển đổi từ String (ví dụ từ DB) sang Enum
         */
        public static UserRole fromString(String roleStr) {
            for (UserRole role : UserRole.values()) {
                if (role.name().equalsIgnoreCase(roleStr)) {
                    return role;
                }
            }
            return GUEST; // Mặc định nếu không xác định được
        }

        @Override
        public String toString() {
            return description;
        }
    }
