package com.studysync.factory;

import com.studysync.model.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * FACTORY PATTERN: the controller and service never write "new OnlineSession()" themselves.
 * They ask the factory for a session of a TYPE, and the factory decides which class to create
 * and checks the rules for that type (online needs a link, physical needs a venue, ...).
 */
@Component
public class SessionFactory {

    public static final int DEFAULT_DURATION = 60;
    public static final int EXAM_REVISION_DURATION = 120;

    public StudySession createSession(SessionType type, String title, StudyGroup group, Student creator,
                                      LocalDateTime startTime, int durationMinutes,
                                      String place, String examTopic) {
        if (type == null) {
            throw new IllegalArgumentException("Please choose a session type.");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Session title is required.");
        }
        if (startTime == null) {
            throw new IllegalArgumentException("Please choose the date and time.");
        }

        StudySession session;
        switch (type) {
            case ONLINE -> {
                if (place == null || !(place.startsWith("http://") || place.startsWith("https://"))) {
                    throw new IllegalArgumentException("Online sessions need a meeting link starting with https://");
                }
                session = new OnlineSession(place.trim());
            }
            case PHYSICAL -> {
                if (place == null || place.isBlank()) {
                    throw new IllegalArgumentException("Physical sessions need a venue, e.g. Library Room 2.");
                }
                session = new PhysicalSession(place.trim());
            }
            case EXAM_REVISION -> {
                if (examTopic == null || examTopic.isBlank()) {
                    throw new IllegalArgumentException("Exam revision sessions need an exam topic.");
                }
                String location = (place == null || place.isBlank()) ? "To be decided" : place.trim();
                session = new ExamRevisionSession(examTopic.trim(), location);
                if (durationMinutes <= 0) {
                    durationMinutes = EXAM_REVISION_DURATION;
                }
            }
            default -> throw new IllegalArgumentException("Unknown session type: " + type);
        }

        session.setTitle(title.trim());
        session.setGroup(group);
        session.setCreatedBy(creator);
        session.setStartTime(startTime);
        session.setDurationMinutes(durationMinutes > 0 ? durationMinutes : DEFAULT_DURATION);
        return session;
    }
}