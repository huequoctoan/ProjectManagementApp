package com.project.repositories;

import com.project.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    // Tìm các task thuộc về một dự án
    List<Task> findByProjectId(Long projectId);

}