package com.project.services;

import com.project.dao.TaskDAO;
import com.project.entities.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskDAO taskDAO;

    public List<Task> getAllTasks() {
        return taskDAO.findAll();
    }

    public Task getTaskById(Long id) {
        return taskDAO.findById(id).orElse(null);
    }

    public List<Task> getTasksByProjectId(Long projectId){
        return taskDAO.findByProjectId(projectId);
    }


    public Task createTask(Task task) {
        return taskDAO.save(task);
    }

    public Task updateTask(Long id, Task details) {
        Task existingTask = taskDAO.findById(id).orElse(null);
        if (existingTask != null) {
            if (details.getTitle() != null) existingTask.setTitle(details.getTitle());
            if (details.getDescription() != null) existingTask.setDescription(details.getDescription());
            if (details.getStatus() != null) existingTask.setStatus(details.getStatus());
            if (details.getDueDate() != null) existingTask.setDueDate(details.getDueDate());
            if (details.getProject() != null) existingTask.setProject(details.getProject());
            if (details.getAssignedMembers() != null) {
                existingTask.getAssignedMembers().clear();
                existingTask.getAssignedMembers().addAll(details.getAssignedMembers());
            }
            return taskDAO.save(existingTask);
        }
        return null;
    }

    public void deleteTask(Long id) {
        taskDAO.deleteById(id);
    }
}