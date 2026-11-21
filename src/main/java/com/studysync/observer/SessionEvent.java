package com.studysync.observer;

import com.studysync.model.StudySession;

/** What happened to a session. Sent from the Subject (SessionNotifier) to every observer. */
public class SessionEvent {

    public enum Type {
        CREATED,
        UPDATED,
        REMINDER
    }

    private final Type type;
    private final StudySession session;
    private final String message;

    public SessionEvent(Type type, StudySession session, String message) {
        this.type = type;
        this.session = session;
        this.message = message;
    }

    public Type getType() {
        return type;
    }

    public StudySession getSession() {
        return session;
    }

    public String getMessage() {
        return message;
    }
}