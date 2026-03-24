package com.project.dao;

import com.project.entities.Project;
import java.util.List;
import java.util.Optional;

public interface ProjectDAO {
    Project save(Project project);
    Optional<Project> findById(Long id);
    List<Project> findAll();
    void deleteById(Long id);
}
