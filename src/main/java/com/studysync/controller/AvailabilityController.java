package com.studysync.controller;

import com.studysync.config.LoginInterceptor;
import com.studysync.service.AvailabilityService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/** The availability calendar page. */
@Controller
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @GetMapping("/availability")
    public String availability(HttpSession httpSession, Model model) {
        model.addAttribute("slots", availabilityService.getSlots(LoginInterceptor.currentStudentId(httpSession)));
        model.addAttribute("days", DayOfWeek.values());
        return "availability";
    }

    @PostMapping("/availability")
    public String addSlot(@RequestParam("day") String day,
                          @RequestParam("startTime") String startTime,
                          @RequestParam("endTime") String endTime,
                          HttpSession httpSession, RedirectAttributes redirect) {
        try {
            availabilityService.addSlot(LoginInterceptor.currentStudentId(httpSession),
                    DayOfWeek.valueOf(day), LocalTime.parse(startTime), LocalTime.parse(endTime));
            redirect.addFlashAttribute("success", "Free time added.");
        } catch (IllegalArgumentException | DateTimeParseException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/availability";
    }

    @PostMapping("/availability/{id}/delete")
    public String deleteSlot(@PathVariable("id") Long id, HttpSession httpSession, RedirectAttributes redirect) {
        try {
            availabilityService.removeSlot(id, LoginInterceptor.currentStudentId(httpSession));
            redirect.addFlashAttribute("success", "Time slot removed.");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/availability";
    }
}