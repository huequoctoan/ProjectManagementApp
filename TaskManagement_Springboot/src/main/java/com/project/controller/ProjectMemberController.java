package com.project.controller;

import com.project.entities.ProjectMember;
import com.project.services.ProjectMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/project-members")
public class ProjectMemberController {

    @Autowired
    private ProjectMemberService projectMemberService;

    @GetMapping
    public List<ProjectMember> getAll() {
        return projectMemberService.getAllProjectMembers();
    }

    @GetMapping("/{id}")
    public ProjectMember getById(@PathVariable Long id) {
        return projectMemberService.getProjectMemberById(id);
    }

    @PostMapping
    public ProjectMember create(@RequestBody ProjectMember projectMember) {
        return projectMemberService.createProjectMember(projectMember);
    }

    @PutMapping("/{id}")
    public ProjectMember update(@PathVariable Long id, @RequestBody ProjectMember details) {
        return projectMemberService.updateProjectMember(id, details);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        projectMemberService.deleteProjectMember(id);
    }

    @GetMapping("/project/{projectId}")
    public List<ProjectMember> getMembersByProject(@PathVariable Long projectId) {
        return projectMemberService.getMembersByProjectId(projectId);
    }

    /**
     * Push Model: Thêm thành viên vào dự án bằng username hoặc email.
     * POST /api/project-members/project/{projectId}/add
     * Body: { "usernameOrEmail": "nguoidung123" }
     */
    @PostMapping("/project/{projectId}/add")
    public ProjectMember addMember(
            @PathVariable Long projectId,
            @RequestBody Map<String, String> body) {

        String usernameOrEmail = body.get("usernameOrEmail");
        if (usernameOrEmail == null || usernameOrEmail.isBlank()) {
            throw new RuntimeException("Vui lòng nhập username hoặc email");
        }
        return projectMemberService.addMemberByUsernameOrEmail(projectId, usernameOrEmail);
    }
}