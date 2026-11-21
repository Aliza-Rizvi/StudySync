package com.studysync.controller;

import com.studysync.config.LoginInterceptor;
import com.studysync.service.GroupService;
import com.studysync.service.SessionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final SessionService sessionService;
    private final GroupService groupService;

    public DashboardController(SessionService sessionService, GroupService groupService) {
        this.sessionService = sessionService;
        this.groupService = groupService;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession httpSession, Model model) {
        Long me = LoginInterceptor.currentStudentId(httpSession);
        model.addAttribute("upcoming", sessionService.upcomingForStudent(me));
        model.addAttribute("groups", groupService.myGroups(me));
        model.addAttribute("suggestedGroups", groupService.suggestGroups(me));
        model.addAttribute("invites", groupService.pendingInvitesFor(me));
        model.addAttribute("attendedCount", sessionService.attendanceCount(me));
        model.addAttribute("myRating", sessionService.averageRatingFor(me));
        return "dashboard";
    }
}