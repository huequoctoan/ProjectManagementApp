package com.project.services;

import com.project.entities.BoardColumn;
import com.project.entities.Project;
import com.project.repositories.BoardColumnRepository;
import com.project.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BoardColumnService {

    @Autowired
    private BoardColumnRepository columnRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public List<BoardColumn> getColumnsByProjectId(Long projectId) {
        return columnRepository.findByProjectIdOrderByPositionAsc(projectId);
    }

    public BoardColumn createColumn(Long projectId, BoardColumn column) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        

        List<BoardColumn> existing = columnRepository.findByProjectIdOrderByPositionAsc(projectId);
        int nextPos = existing.size() > 0 ? existing.get(existing.size() - 1).getPosition() + 1 : 0;
        
        column.setProject(project);
        column.setPosition(nextPos);
        if (column.getColor() == null) {
            column.setColor("#9CA3AF"); // Default gray
        }
        
        return columnRepository.save(column);
    }

    public void createDefaultColumns(Project project) {
        BoardColumn today = new BoardColumn(null, "Today", "#d9f99d", 0, project);
        BoardColumn thisWeek = new BoardColumn(null, "Week", "#bbf7d0", 1, project);
        BoardColumn later = new BoardColumn(null, "Later", "#f8fafc", 2, project);
        
        columnRepository.saveAll(List.of(today, thisWeek, later));
    }
}
