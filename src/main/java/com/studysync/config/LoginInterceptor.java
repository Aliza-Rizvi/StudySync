package com.studysync.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Runs BEFORE every controller. If nobody is logged in, it sends the browser to /login.
 * After login we keep the student's id in the HTTP session under the key "studentId".
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    public static final String SESSION_KEY = "studentId";

    /** Id of the logged-in student, or null */
    public static Long currentStudentId(HttpSession httpSession) {
        Object value = httpSession.getAttribute(SESSION_KEY);
        return (value instanceof Long id) ? id : null;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession httpSession = request.getSession(false);
        if (httpSession != null && httpSession.getAttribute(SESSION_KEY) != null) {
            return true; // logged in: continue to the controller
        }
        response.sendRedirect(request.getContextPath() + "/login");
        return false; // not logged in: stop here
    }
}