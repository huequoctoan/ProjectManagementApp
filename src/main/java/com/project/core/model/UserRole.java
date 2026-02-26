package com.project.core.model;

public enum UserRole {
    ADMIN("Admin"),
    MANAGER("Manager"),
    EMPLOYEE("Employee");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}