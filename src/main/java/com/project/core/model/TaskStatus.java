package com.project.core.model;

public enum TaskStatus {
    TODO("To do"),
    IN_PROGRESS("In Progress"),
    DONE("Done"),
    CANCEL("Cancelled");

    private final String label;

    TaskStatus(String label) {
        this.label = label;
    }

    public String getLabel(){
        return label;
    }
}
