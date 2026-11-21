package com.studysync.observer;

import com.studysync.model.Notification;
import com.studysync.model.Student;
import com.studysync.model.StudySession;
import com.studysync.repository.NotificationRepository;
import org.springframework.stereotype.Component;

/** Concrete observer 1: saves a notification for EVERY member of the session's group. */
@Component
public class NotificationObserver implements SessionObserver {

    private final NotificationRepository notificationRepository;

    public NotificationObserver(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void update(SessionEvent event) {
        StudySession session = event.getSession();
        for (Student member : session.getGroup().getMembers()) {
            notificationRepository.save(new Notification(member, event.getMessage(), "/sessions/" + session.getId()));
        }
    }
}