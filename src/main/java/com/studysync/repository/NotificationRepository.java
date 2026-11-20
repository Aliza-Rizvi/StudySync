package com.studysync.repository;

import com.studysync.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByStudent_IdOrderByCreatedAtDesc(Long studentId);

    long countByStudent_IdAndSeenFalse(Long studentId);
}