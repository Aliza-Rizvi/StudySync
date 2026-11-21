package com.studysync.controller;

import com.studysync.config.LoginInterceptor;
import com.studysync.service.GroupService;
import com.studysync.service.MatchingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** Find Partners page: the student chooses a course and a matching strategy. */
@Controller
public class PartnerController {

    private final MatchingService matchingService;
    private final GroupService groupService;

    public PartnerController(MatchingService matchingService, GroupService groupService) {
        this.matchingService = matchingService;
        this.groupService = groupService;
    }

    @GetMapping("/partners")
    public String partners(@RequestParam(value = "courseId", required = false) Long courseId,
                           @RequestParam(value = "strategy", required = false) String strategyKey,
                           HttpSession httpSession, Model model) {
        Long me = LoginInterceptor.currentStudentId(httpSession);
        String chosenKey = matchingService.getStrategy(strategyKey).getKey();
        model.addAttribute("strategies", matchingService.getStrategies());
        model.addAttribute("selectedStrategy", chosenKey);
        model.addAttribute("selectedCourseId", courseId);
        model.addAttribute("results", matchingService.findPartners(me, courseId, chosenKey));
        model.addAttribute("myGroups", groupService.myGroups(me));
        return "partners";
    }
}