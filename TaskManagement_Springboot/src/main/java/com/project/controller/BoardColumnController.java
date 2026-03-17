package com.project.controller;

import com.project.entities.BoardColumn;
import com.project.services.BoardColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/columns")
public class BoardColumnController {

    @Autowired
    private BoardColumnService columnService;

    @GetMapping("/project/{projectId}")
    public List<BoardColumn> getColumnsByProject(@PathVariable Long projectId) {
        return columnService.getColumnsByProjectId(projectId);
    }

    @PostMapping("/project/{projectId}")
    public BoardColumn createColumn(@PathVariable Long projectId, @RequestBody BoardColumn column) {
        return columnService.createColumn(projectId, column);
    }
}
