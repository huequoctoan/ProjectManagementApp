package com.project.core.model;

import java.util.Objects;

public class User {
    // 1. Thuộc tính định danh duy nhất (Immutability)
    private final String id;
    private String username;
    private String fullName;
    private String email;
    private String role; //

    // 2. Constructor đầy đủ
    public User(String id, String username, String fullName, String email, String role) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        this.id = id;
        this.setUsername(username);
        this.fullName = fullName;
        this.setEmail(email);
        this.role = (role != null) ? role : "GUEST";
    }

    // 3. Constructor rút gọn cho các thao tác nhanh
    public User(String id, String username) {
        this(id, username, "", "", "GUEST");
    }

    // --- Getters & Setters với Validation ---

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        // Validation đơn giản cho email
        if (email != null && !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
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