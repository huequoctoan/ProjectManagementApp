package com.project.services;

import com.project.entities.Project;
import com.project.entities.ProjectMember;
import com.project.repositories.ProjectMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectMemberService {

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    public List<ProjectMember> getAllProjectMembers() {
        return projectMemberRepository.findAll();
    }

    public ProjectMember getProjectMemberById(Long id) {
        return projectMemberRepository.findById(id).orElse(null);
    }

    public List<ProjectMember> getMembersByProjectId(Long projectId){
        return projectMemberRepository.findByProjectId(projectId);
    }

    public ProjectMember createProjectMember(ProjectMember projectMember) {
        return projectMemberRepository.save(projectMember);
    }

    public ProjectMember updateProjectMember(Long id, ProjectMember details) {
        ProjectMember existingMember = projectMemberRepository.findById(id).orElse(null);
        if (existingMember != null) {
            existingMember.setRole(details.getRole());
            // Cập nhật lại User và Project nếu cần thiết
            existingMember.setUser(details.getUser());
            existingMember.setProject(details.getProject());
            return projectMemberRepository.save(existingMember);
        }
        return null;
    }

    public void deleteProjectMember(Long id) {
        projectMemberRepository.deleteById(id);
    }
}