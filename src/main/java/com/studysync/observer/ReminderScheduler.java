package com.studysync.observer;

import com.studysync.config.ConfigurationManager;
import com.studysync.model.StudySession;
import com.studysync.repository.SessionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Every minute: finds sessions starting in the next 24 hours that did not get a reminder,
 * and sends a REMINDER event through the SessionNotifier (so all observers react).
 */
@Component
public class ReminderScheduler {

    private static final DateTimeFormatter NICE_TIME = DateTimeFormatter.ofPattern("dd MMM, hh:mm a");

    private final SessionRepository sessionRepository;
    private final SessionNotifier sessionNotifier;

    public ReminderScheduler(SessionRepository sessionRepository, SessionNotifier sessionNotifier) {
        this.sessionRepository = sessionRepository;
        this.sessionNotifier = sessionNotifier;
    }

    @Scheduled(fixedRate = 60000)
    public void sendReminders() {
        int hours = ConfigurationManager.getInstance().getReminderHoursBefore();
        LocalDateTime now = LocalDateTime.now();
        List<StudySession> dueSoon = sessionRepository.findByReminderSentFalseAndStartTimeBetween(now, now.plusHours(hours));
        for (StudySession session : dueSoon) {
            sessionNotifier.notifyObservers(new SessionEvent(SessionEvent.Type.REMINDER, session,
                    "Reminder: \"" + session.getTitle() + "\" starts on " + session.getStartTime().format(NICE_TIME) + "."));
            session.setReminderSent(true);
            sessionRepository.save(session);
        }
    }
}