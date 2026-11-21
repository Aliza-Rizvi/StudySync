package com.studysync.controller;

import com.studysync.config.LoginInterceptor;
import com.studysync.model.Notification;
import com.studysync.service.NotificationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/notifications")
    public String notifications(HttpSession httpSession, Model model) {
        Long me = LoginInterceptor.currentStudentId(httpSession);
        List<Notification> notifications = notificationService.forStudent(me);
        // remember which ones are new BEFORE marking them as seen
        Set<Long> newIds = notifications.stream()
                .filter(notification -> !notification.isSeen())
                .map(Notification::getId)
                .collect(Collectors.toSet());
        notificationService.markAllSeen(me);
        model.addAttribute("notifications", notifications);
        model.addAttribute("newIds", newIds);
        model.addAttribute("unreadCount", 0L);
        return "notifications";
    }
}