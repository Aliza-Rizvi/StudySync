package com.studysync.controller;

import com.studysync.config.LoginInterceptor;
import com.studysync.factory.SessionType;
import com.studysync.model.Attendance;
import com.studysync.model.StudySession;
import com.studysync.service.GroupService;
import com.studysync.service.SessionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/** Schedule sessions, session details (notes, attendance, ratings, reschedule) and history. */
@Controller
public class SessionController {

    private final SessionService sessionService;
    private final GroupService groupService;

    public SessionController(SessionService sessionService, GroupService groupService) {
        this.sessionService = sessionService;
        this.groupService = groupService;
    }

    @GetMapping("/groups/{groupId}/sessions/new")
    public String newSessionForm(@PathVariable("groupId") Long groupId, Model model) {
        model.addAttribute("group", groupService.getGroup(groupId));
        model.addAttribute("types", SessionType.values());
        return "session-new";
    }

    @PostMapping("/groups/{groupId}/sessions")
    public String createSession(@PathVariable("groupId") Long groupId,
                                @RequestParam("type") SessionType type,
                                @RequestParam("title") String title,
                                @RequestParam("startTime") String startTime,
                                @RequestParam(value = "durationMinutes", defaultValue = "0") int durationMinutes,
                                @RequestParam(value = "place", required = false) String place,
                                @RequestParam(value = "examTopic", required = false) String examTopic,
                                HttpSession httpSession, RedirectAttributes redirect) {
        try {
            StudySession session = sessionService.scheduleSession(groupId, LoginInterceptor.currentStudentId(httpSession),
                    type, title, LocalDateTime.parse(startTime), durationMinutes, place, examTopic);
            redirect.addFlashAttribute("success", "Session scheduled. All group members were notified.");
            return "redirect:/sessions/" + session.getId();
        } catch (IllegalArgumentException | DateTimeParseException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/groups/" + groupId + "/sessions/new";
        }
    }

    @GetMapping("/sessions/{id}")
    public String sessionDetails(@PathVariable("id") Long id, HttpSession httpSession, Model model) {
        Long me = LoginInterceptor.currentStudentId(httpSession);
        StudySession session = sessionService.getSession(id);
        Set<Long> presentIds = sessionService.attendanceFor(id).stream()
                .filter(Attendance::isPresent)
                .map(attendance -> attendance.getStudent().getId())
                .collect(Collectors.toSet());
        model.addAttribute("studySession", session);
        model.addAttribute("notes", sessionService.notesFor(id));
        model.addAttribute("ratings", sessionService.ratingsFor(id));
        model.addAttribute("presentIds", presentIds);
        model.addAttribute("attendanceTaken", !sessionService.attendanceFor(id).isEmpty());
        model.addAttribute("isMember", session.getGroup().hasMember(me));
        model.addAttribute("isCreator", session.getCreatedBy().getId().equals(me));
        model.addAttribute("me", me);
        return "session-detail";
    }

    @PostMapping("/sessions/{id}/notes")
    public String addNote(@PathVariable("id") Long id,
                          @RequestParam("title") String title,
                          @RequestParam("url") String url,
                          HttpSession httpSession, RedirectAttributes redirect) {
        try {
            sessionService.addNoteLink(id, LoginInterceptor.currentStudentId(httpSession), title, url);
            redirect.addFlashAttribute("success", "Notes link added.");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/sessions/" + id;
    }

    @PostMapping("/sessions/{id}/attendance")
    public String saveAttendance(@PathVariable("id") Long id,
                                 @RequestParam(value = "presentIds", required = false) List<Long> presentIds,
                                 HttpSession httpSession, RedirectAttributes redirect) {
        try {
            Set<Long> present = presentIds == null ? new HashSet<>() : new HashSet<>(presentIds);
            sessionService.saveAttendance(id, LoginInterceptor.currentStudentId(httpSession), present);
            redirect.addFlashAttribute("success", "Attendance saved.");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/sessions/" + id;
    }

    @PostMapping("/sessions/{id}/rate")
    public String rate(@PathVariable("id") Long id,
                       @RequestParam(value = "ratedStudentId", required = false) Long ratedStudentId,
                       @RequestParam("stars") int stars,
                       @RequestParam(value = "comment", required = false) String comment,
                       HttpSession httpSession, RedirectAttributes redirect) {
        try {
            sessionService.rate(id, LoginInterceptor.currentStudentId(httpSession), ratedStudentId, stars, comment);
            redirect.addFlashAttribute("success", "Thanks for your feedback!");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/sessions/" + id;
    }

    @PostMapping("/sessions/{id}/reschedule")
    public String reschedule(@PathVariable("id") Long id,
                             @RequestParam("startTime") String startTime,
                             HttpSession httpSession, RedirectAttributes redirect) {
        try {
            sessionService.reschedule(id, LoginInterceptor.currentStudentId(httpSession), LocalDateTime.parse(startTime));
            redirect.addFlashAttribute("success", "Session moved. All members were notified.");
        } catch (IllegalArgumentException | DateTimeParseException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/sessions/" + id;
    }

    @GetMapping("/history")
    public String history(HttpSession httpSession, Model model) {
        Long me = LoginInterceptor.currentStudentId(httpSession);
        model.addAttribute("upcoming", sessionService.upcomingForStudent(me));
        model.addAttribute("past", sessionService.pastForStudent(me));
        return "history";
    }
}