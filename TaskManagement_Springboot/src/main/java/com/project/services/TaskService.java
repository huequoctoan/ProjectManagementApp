package com.project.services;

import com.project.entities.Task;
import com.project.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    public List<Task> getTasksByProjectId(Long projectId){
        return taskRepository.findByProjectId(projectId);
    }


    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task details) {
        Task existingTask = taskRepository.findById(id).orElse(null);
        if (existingTask != null) {
            if (details.getTitle() != null) existingTask.setTitle(details.getTitle());
            if (details.getDescription() != null) existingTask.setDescription(details.getDescription());
            if (details.getDueDate() != null) existingTask.setDueDate(details.getDueDate());
            if (details.getProject() != null) existingTask.setProject(details.getProject());
            if (details.getAssignedMembers() != null) {
                existingTask.getAssignedMembers().clear();
                existingTask.getAssignedMembers().addAll(details.getAssignedMembers());
            }
            return taskRepository.save(existingTask);
        }
        return null;
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}