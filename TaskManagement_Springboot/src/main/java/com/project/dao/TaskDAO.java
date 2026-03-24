package com.project.dao;

import com.project.entities.Task;
import java.util.List;
import java.util.Optional;

public interface TaskDAO {
    Task save(Task task);
    Optional<Task> findById(Long id);
    List<Task> findByProjectId(Long projectId);
    List<Task> findAll();
    void deleteById(Long id);
}
