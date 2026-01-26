package com.project.core.model;

import java.time.LocalDate;

public class Task {
    private String id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDate deadline;

    public Task(String id, String title, TaskStatus status) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.deadline = LocalDate.now().plusDays(7); // mac dinh 7 ngay, dam bao moi task luon co thoi han
    }

    // --- Getters & Setters ---
    public String getId() { return id; } // Khong duoc sua id
    public String getTitle() { return title; } // Title va TaskStatus co the sua duoc
    public void setTitle(String title) {
        this.title = title;
    }
    public TaskStatus getStatus() {
        return status;
    }
    public void setStatus(TaskStatus status) {
        this.status = status;
    }
    public LocalDate getDeadline() {
        return deadline;
    }
    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
}
