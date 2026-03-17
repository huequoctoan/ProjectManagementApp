package com.project.repositories;

import com.project.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    // For manual cascade deletes
    List<Notification> findByProjectId(Long projectId);
    List<Notification> findByUserId(Long userId);
}
