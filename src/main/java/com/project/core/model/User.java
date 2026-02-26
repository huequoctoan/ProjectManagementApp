package com.project.core.model;

import java.util.Objects;

public class User {
    private final String id;
    private String username;
    private String fullName;
    private String email;
    private UserRole role; // Thay đổi từ String sang UserRole

    // Constructor đầy đủ
    public User(String id, String username, String fullName, String email, UserRole role) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        this.id = id;
        this.setUsername(username);
        this.fullName = fullName;
        this.setEmail(email);
        // Mặc định nếu null sẽ là EMPLOYEE
        this.role = (role != null) ? role : UserRole.EMPLOYEE;
    }

    // Constructor rút gọn
    public User(String id, String username) {
        this(id, username, "", "", UserRole.EMPLOYEE);
    }

    // --- Getters & Setters ---

    public String getId() { return id; }

    public String getUsername() { return username; }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        this.username = username;
    }

    public String getFullName() { return fullName; }

    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }

    public void setEmail(String email) {
        if (email != null && !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email;
    }

    public UserRole getRole() { return role; }

    public void setRole(UserRole role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return fullName != null && !fullName.isEmpty() ? fullName : username;
    }
}