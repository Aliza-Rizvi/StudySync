package com.studysync.controller;

import com.studysync.config.ConfigurationManager;
import com.studysync.config.LoginInterceptor;
import com.studysync.service.NotificationService;
import com.studysync.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/** Adds the app name, the logged-in student and the unread count to EVERY page (used by the navbar). */
@ControllerAdvice
public class GlobalModelAdvice {

    private final StudentService studentService;
    private final NotificationService notificationService;

    public GlobalModelAdvice(StudentService studentService, NotificationService notificationService) {
        this.studentService = studentService;
        this.notificationService = notificationService;
    }

    @ModelAttribute
    public void addCommonData(Model model, HttpSession httpSession) {
        model.addAttribute("appName", ConfigurationManager.getInstance().getAppName());
        Long studentId = LoginInterceptor.currentStudentId(httpSession);
        if (studentId != null) {
            model.addAttribute("currentStudent", studentService.getById(studentId));
            model.addAttribute("unreadCount", notificationService.unreadCount(studentId));
        }
    }
}