package com.project.services;


import com.project.entities.Project;
import com.project.entities.ProjectMember;
import com.project.repositories.ProjectMemberRepository;
import com.project.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    public List<Project> getAllProject(){
        return projectRepository.findAll();
    }

    public Project createProject(Project project){
        return projectRepository.save(project);
    }

    public Project findProject(Long id){
        return projectRepository.findById(id).orElseThrow(() ->
                                                new ResponseStatusException(HttpStatus.NOT_FOUND,"Khong tim thay Project"));
    }

    public void deleteProject(Long id){
        if(!projectRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Khong tim thay Project");
        }
        projectRepository.deleteById(id);
    }

    public List<Project> getProjectsByUserId(Long userId){

        List<ProjectMember> members = projectMemberRepository.findByUserId(userId);

        return members.stream()
                .map(ProjectMember::getProject)
                .toList();
    }
}
