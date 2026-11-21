package com.studysync.controller;

import com.studysync.config.LoginInterceptor;
import com.studysync.model.StudyStyle;
import com.studysync.service.CourseService;
import com.studysync.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/** Profile details and course enrollment. */
@Controller
public class ProfileController {

    private final StudentService studentService;
    private final CourseService courseService;

    public ProfileController(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        model.addAttribute("allCourses", courseService.findAll());
        model.addAttribute("styles", StudyStyle.values());
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@RequestParam("fullName") String fullName,
                                @RequestParam("semester") int semester,
                                @RequestParam(value = "department", required = false) String department,
                                @RequestParam("cgpa") double cgpa,
                                @RequestParam("studyStyle") StudyStyle studyStyle,
                                HttpSession httpSession, RedirectAttributes redirect) {
        try {
            studentService.updateProfile(LoginInterceptor.currentStudentId(httpSession),
                    fullName, semester, department, cgpa, studyStyle);
            redirect.addFlashAttribute("success", "Profile saved.");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/profile";
    }

    @PostMapping("/profile/courses")
    public String updateCourses(@RequestParam(value = "courseIds", required = false) List<Long> courseIds,
                                HttpSession httpSession, RedirectAttributes redirect) {
        studentService.updateCourses(LoginInterceptor.currentStudentId(httpSession), courseIds);
        redirect.addFlashAttribute("success", "Courses saved. Next: add your free time on the Availability page.");
        return "redirect:/profile";
    }
}