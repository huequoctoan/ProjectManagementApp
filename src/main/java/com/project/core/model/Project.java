package com.project.core.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Project {
    // 1. Thuộc tính cơ bản
    private final String id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private ProjectStatus status;

    // 2. Mối quan hệ: Một Project có nhiều Tasks
    private List<Task> tasks;

    // 3. Người chịu trách nhiệm (Dùng model User vừa tạo)
    private User owner;

    public enum ProjectStatus {
        PLANNING, ONGOING, COMPLETED, ON_HOLD
    }


    public Project(String id, String name, User owner) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Project ID cannot be null");
        }
        this.id = id;
        this.setName(name);
        this.owner = owner;
        this.tasks = new ArrayList<>();
        this.startDate = LocalDate.now();
        this.status = ProjectStatus.PLANNING;
    }

    public int calculateProgress() {
        if (tasks.isEmpty()) return 0;

        long completedCount = tasks.stream()
                .filter(t -> t.getStatus() == TaskStatus.DONE)
                .count();

        return (int) ((completedCount * 100) / tasks.size());
    }

    public void addTask(Task task) {
        if (task != null && !tasks.contains(task)) {
            tasks.add(task);
        }
    }

    // --- Getters & Setters ---

    public String getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Project name cannot be empty");
        }
        this.name = name;
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks);
    }

    public void setEndDate(LocalDate endDate) {
        if (endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        this.endDate = endDate;
    }

    public ProjectStatus getStatus() { return status; }

    public void setStatus(ProjectStatus status) { this.status = status; }

    public User getOwner() { return owner; }

    public void setOwner(User owner) { this.owner = owner; }

    // --- Standard Methods ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(id, project.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name + " (" + calculateProgress() + "%)";
    }
}