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
        BoardColumn todo = new BoardColumn(null, "To Do", "#E2E8F0", 0, project);
        BoardColumn inProgress = new BoardColumn(null, "In Progress", "#BAE6FD", 1, project);
        BoardColumn done = new BoardColumn(null, "Done", "#BBF7D0", 2, project);
        
        columnRepository.saveAll(List.of(todo, inProgress, done));
    }

    public BoardColumn updateColumn(Long id, BoardColumn columnData) {
        BoardColumn existing = columnRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Column not found"));
        
        if (columnData.getName() != null) existing.setName(columnData.getName());
        if (columnData.getColor() != null) existing.setColor(columnData.getColor());
        if (columnData.getPosition() != -1) existing.setPosition(columnData.getPosition());
        
        return columnRepository.save(existing);
    }
}
