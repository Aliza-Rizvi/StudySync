package com.studysync.controller;

import com.studysync.config.LoginInterceptor;
import com.studysync.model.Student;
import com.studysync.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Home, register, login and logout. */
@Controller
public class AuthController {

    private final StudentService studentService;

    public AuthController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/")
    public String home(HttpSession httpSession) {
        return LoginInterceptor.currentStudentId(httpSession) != null ? "redirect:/dashboard" : "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        HttpSession httpSession, RedirectAttributes redirect) {
        try {
            Student student = studentService.login(email, password);
            httpSession.setAttribute(LoginInterceptor.SESSION_KEY, student.getId());
            return "redirect:/dashboard";
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        }
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam("fullName") String fullName,
                           @RequestParam("email") String email,
                           @RequestParam("password") String password,
                           @RequestParam(value = "sapId", required = false) String sapId,
                           @RequestParam(value = "semester", defaultValue = "1") int semester,
                           @RequestParam(value = "department", required = false) String department,
                           HttpSession httpSession, RedirectAttributes redirect) {
        try {
            Student student = studentService.register(fullName, email, password, sapId, semester, department);
            httpSession.setAttribute(LoginInterceptor.SESSION_KEY, student.getId());
            redirect.addFlashAttribute("success", "Welcome to StudySync! Now choose your courses and study style.");
            return "redirect:/profile";
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.invalidate();
        return "redirect:/login";
    }
}