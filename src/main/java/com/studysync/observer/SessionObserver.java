package com.studysync.observer;

/**
 * OBSERVER PATTERN: every class that wants to hear about session changes implements this.
 * The Subject (SessionNotifier) calls update() on all of them.
 */
public interface SessionObserver {
    void update(SessionEvent event);
}