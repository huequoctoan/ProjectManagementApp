package com.project.controller;

import com.project.entities.ProjectMember;
import com.project.services.ProjectMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<ProjectMember> getMembersByProject(@PathVariable Long projectId){
        return projectMemberService.getMembersByProjectId(projectId);
    }

}