package com.project.controller;

import com.project.entities.Comment;
import com.project.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "http://localhost:4200")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public Comment addComment(@RequestBody Map<String, Object> payload) {
        Long taskId = Long.valueOf(payload.get("taskId").toString());
        Long userId = Long.valueOf(payload.get("userId").toString());
        String content = payload.get("content").toString();
        return commentService.addComment(taskId, userId, content);
    }

    @GetMapping("/task/{taskId}")
    public List<Comment> getTaskComments(@PathVariable Long taskId) {
        return commentService.getTaskComments(taskId);
    }
}
