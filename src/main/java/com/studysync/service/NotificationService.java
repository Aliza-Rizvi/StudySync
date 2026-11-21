package com.studysync.service;

import com.studysync.model.Notification;
import com.studysync.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> forStudent(Long studentId) {
        return notificationRepository.findByStudent_IdOrderByCreatedAtDesc(studentId);
    }

    public long unreadCount(Long studentId) {
        return notificationRepository.countByStudent_IdAndSeenFalse(studentId);
    }

    public void markAllSeen(Long studentId) {
        List<Notification> notifications = notificationRepository.findByStudent_IdOrderByCreatedAtDesc(studentId);
        for (Notification notification : notifications) {
            if (!notification.isSeen()) {
                notification.setSeen(true);
                notificationRepository.save(notification);
            }
        }
    }
}