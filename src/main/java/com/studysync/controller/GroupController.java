package com.studysync.controller;

import com.studysync.config.ConfigurationManager;
import com.studysync.config.LoginInterceptor;
import com.studysync.model.StudyGroup;
import com.studysync.service.GroupService;
import com.studysync.service.SessionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Groups: list, create, details, join requests, invitations, leave. */
@Controller
public class GroupController {

    private final GroupService groupService;
    private final SessionService sessionService;

    public GroupController(GroupService groupService, SessionService sessionService) {
        this.groupService = groupService;
        this.sessionService = sessionService;
    }

    @GetMapping("/groups")
    public String groups(HttpSession httpSession, Model model) {
        Long me = LoginInterceptor.currentStudentId(httpSession);
        model.addAttribute("myGroups", groupService.myGroups(me));
        model.addAttribute("suggestedGroups", groupService.suggestGroups(me));
        model.addAttribute("joinRequests", groupService.pendingRequestsForCreator(me));
        model.addAttribute("invites", groupService.pendingInvitesFor(me));
        return "groups";
    }

    @GetMapping("/groups/new")
    public String newGroupForm(Model model) {
        model.addAttribute("maxSize", ConfigurationManager.getInstance().getMaxGroupSize());
        return "group-new";
    }

    @PostMapping("/groups")
    public String createGroup(@RequestParam("name") String name,
                              @RequestParam(value = "courseId", required = false) Long courseId,
                              @RequestParam(value = "description", required = false) String description,
                              @RequestParam(value = "maxMembers", defaultValue = "6") int maxMembers,
                              HttpSession httpSession, RedirectAttributes redirect) {
        try {
            StudyGroup group = groupService.createGroup(LoginInterceptor.currentStudentId(httpSession),
                    name, courseId, description, maxMembers);
            redirect.addFlashAttribute("success", "Group created. Now invite partners or schedule a session.");
            return "redirect:/groups/" + group.getId();
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/groups/new";
        }
    }

    @GetMapping("/groups/{id}")
    public String groupDetails(@PathVariable("id") Long id, HttpSession httpSession, Model model) {
        Long me = LoginInterceptor.currentStudentId(httpSession);
        StudyGroup group = groupService.getGroup(id);
        model.addAttribute("group", group);
        model.addAttribute("sessions", sessionService.sessionsForGroup(id));
        model.addAttribute("isMember", group.hasMember(me));
        model.addAttribute("isCreator", group.isCreatedBy(me));
        return "group-detail";
    }

    @PostMapping("/groups/{id}/join")
    public String requestToJoin(@PathVariable("id") Long id, HttpSession httpSession, RedirectAttributes redirect) {
        try {
            groupService.requestToJoin(id, LoginInterceptor.currentStudentId(httpSession));
            redirect.addFlashAttribute("success", "Join request sent. The group creator will accept it.");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/groups/" + id;
    }

    @PostMapping("/groups/invite")
    public String invite(@RequestParam("groupId") Long groupId,
                         @RequestParam("studentId") Long studentId,
                         HttpSession httpSession, RedirectAttributes redirect) {
        try {
            groupService.invite(groupId, LoginInterceptor.currentStudentId(httpSession), studentId);
            redirect.addFlashAttribute("success", "Invitation sent.");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/partners";
    }

    @PostMapping("/requests/{id}/accept")
    public String accept(@PathVariable("id") Long id, HttpSession httpSession, RedirectAttributes redirect) {
        try {
            groupService.acceptRequest(id, LoginInterceptor.currentStudentId(httpSession));
            redirect.addFlashAttribute("success", "Accepted.");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/groups";
    }

    @PostMapping("/requests/{id}/reject")
    public String reject(@PathVariable("id") Long id, HttpSession httpSession, RedirectAttributes redirect) {
        try {
            groupService.rejectRequest(id, LoginInterceptor.currentStudentId(httpSession));
            redirect.addFlashAttribute("success", "Rejected.");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/groups";
    }

    @PostMapping("/groups/{id}/leave")
    public String leave(@PathVariable("id") Long id, HttpSession httpSession, RedirectAttributes redirect) {
        try {
            groupService.leaveGroup(id, LoginInterceptor.currentStudentId(httpSession));
            redirect.addFlashAttribute("success", "You left the group.");
            return "redirect:/groups";
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/groups/" + id;
        }
    }
}