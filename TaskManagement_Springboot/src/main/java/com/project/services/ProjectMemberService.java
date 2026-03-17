package com.project.services;

import com.project.entities.Project;
import com.project.entities.ProjectMember;
import com.project.entities.User;
import com.project.repositories.ProjectMemberRepository;
import com.project.repositories.ProjectRepository;
import com.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectMemberService {

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private NotificationService notificationService;

    public List<ProjectMember> getAllProjectMembers() {
        return projectMemberRepository.findAll();
    }

    public ProjectMember getProjectMemberById(Long id) {
        return projectMemberRepository.findById(id).orElse(null);
    }

    public List<ProjectMember> getMembersByProjectId(Long projectId) {
        return projectMemberRepository.findByProjectId(projectId);
    }

    public ProjectMember createProjectMember(ProjectMember projectMember) {
        return projectMemberRepository.save(projectMember);
    }

    public ProjectMember updateProjectMember(Long id, ProjectMember details) {
        ProjectMember existingMember = projectMemberRepository.findById(id).orElse(null);
        if (existingMember != null) {
            existingMember.setRole(details.getRole());
            existingMember.setUser(details.getUser());
            existingMember.setProject(details.getProject());
            return projectMemberRepository.save(existingMember);
        }
        return null;
    }

    public void deleteProjectMember(Long id) {
        projectMemberRepository.deleteById(id);
    }


    public ProjectMember addMemberByUsernameOrEmail(Long projectId, String usernameOrEmail) {

        // 1. Tìm project
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy dự án"));

        // 2. Tìm user theo username hoặc email
        Optional<User> userOpt = userRepository.findByUsername(usernameOrEmail);
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByEmail(usernameOrEmail);
        }

        User user = userOpt.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Không tìm thấy người dùng với username/email: " + usernameOrEmail));

        // 3. Kiểm tra đã là thành viên chưa
        boolean alreadyMember = projectMemberRepository.findByProjectId(projectId)
                .stream()
                .anyMatch(m -> m.getUser().getId().equals(user.getId()));

        if (alreadyMember) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Người dùng đã là thành viên của dự án này");
        }

        // 4. Tạo thành viên mới với role MEMBER
        ProjectMember newMember = new ProjectMember();
        newMember.setProject(project);
        newMember.setUser(user);
        newMember.setRole("MEMBER");

        ProjectMember savedMember = projectMemberRepository.save(newMember);
        
        // 5. Fire Notification
        String msg = "Bạn đã được thêm vào dự án: " + project.getName();
        notificationService.createNotification(user, project, msg);
        
        return savedMember;
    }
}