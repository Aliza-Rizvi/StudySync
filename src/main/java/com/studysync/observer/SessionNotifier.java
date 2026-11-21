package com.studysync.observer;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * OBSERVER PATTERN - the SUBJECT.
 * It keeps a list of observers. When a session is created, changed or about to start,
 * notifyObservers() tells every observer. The Subject does not know what each observer does.
 * Spring automatically gives it all @Component classes that implement SessionObserver.
 */
@Component
public class SessionNotifier {

    private final List<SessionObserver> observers = new ArrayList<>();

    public SessionNotifier(List<SessionObserver> startingObservers) {
        this.observers.addAll(startingObservers);
    }

    public void attach(SessionObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void detach(SessionObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(SessionEvent event) {
        for (SessionObserver observer : observers) {
            observer.update(event);
        }
    }

    public int getObserverCount() {
        return observers.size();
    }
}