package com.project.controller;

import com.project.entities.Notification;
import com.project.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // Lấy thông báo của user
    @GetMapping("/user/{userId}")
    public List<Notification> getUserNotifications(@PathVariable Long userId) {
        return notificationService.getNotificationsForUser(userId);
    }

    // Đánh dấu 1 thông báo là đã đọc
    @PutMapping("/{id}/read/user/{userId}")
    public Notification markAsRead(@PathVariable Long id, @PathVariable Long userId) {
        return notificationService.markAsRead(id, userId);
    }
    
    // Đánh dấu tất cả thông báo của 1 user là đã đọc
    @PutMapping("/user/{userId}/read-all")
    public void markAllAsRead(@PathVariable Long userId) {
        notificationService.markAllAsRead(userId);
    }
}
