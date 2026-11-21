package com.studysync.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/** Concrete observer 2: writes every session event to the IntelliJ console (an activity log). */
@Component
public class ActivityLogObserver implements SessionObserver {

    private static final Logger log = LoggerFactory.getLogger(ActivityLogObserver.class);

    @Override
    public void update(SessionEvent event) {
        log.info("[Observer] {} | group: {} | {}", event.getType(),
                event.getSession().getGroup().getName(), event.getMessage());
    }
}