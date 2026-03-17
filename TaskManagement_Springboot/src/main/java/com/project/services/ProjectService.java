package com.project.services;


import com.project.entities.Project;
import com.project.entities.ProjectMember;
import com.project.entities.User;
import com.project.repositories.ProjectMemberRepository;
import com.project.repositories.ProjectRepository;
import com.project.repositories.TaskRepository;
import com.project.repositories.BoardColumnRepository;
import com.project.repositories.NotificationRepository;
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

    @Autowired
    private UserService userService;

    @Autowired
    private BoardColumnService boardColumnService;

    public List<Project> getAllProject(){
        return projectRepository.findAll();
    }

    public Project createProject(Project project, Long userId){
        Project savedProject = projectRepository.save(project);
        User user = userService.getUserById(userId);

        ProjectMember member = new ProjectMember();
        member.setProject(savedProject);
        member.setUser(user);
        member.setRole("MANAGER");
        projectMemberRepository.save(member);

        // Auto-create default columns for new project
        boardColumnService.createDefaultColumns(savedProject);

        return savedProject;
    }

    public Project findProject(Long id){
        return projectRepository.findById(id).orElseThrow(() ->
                                                new ResponseStatusException(HttpStatus.NOT_FOUND,"Khong tim thay Project"));
    }

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private BoardColumnRepository boardColumnRepository;
    
    @Autowired
    private NotificationRepository notificationRepository;

    public void deleteProject(Long id){
        if(!projectRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Khong tim thay Project");
        }
        
        // Manual Cascade Delete
        // 1. Delete associated Tasks
        taskRepository.deleteAll(taskRepository.findByProjectId(id));
        
        // 2. Delete associated Board Columns
        boardColumnRepository.deleteAll(boardColumnRepository.findByProjectIdOrderByPositionAsc(id));
        
        // 3. Delete associated Notifications
        notificationRepository.deleteAll(notificationRepository.findByProjectId(id));
        
        // 4. Delete associated Project Members
        projectMemberRepository.deleteAll(projectMemberRepository.findByProjectId(id));
        
        // 5. Delete the Project itself
        projectRepository.deleteById(id);
    }

    public List<Project> getProjectsByUserId(Long userId){

        List<ProjectMember> members = projectMemberRepository.findByUserId(userId);

        return members.stream()
                .map(ProjectMember::getProject)
                .toList();
    }
}
