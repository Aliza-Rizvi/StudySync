package com.studysync.observer;

import com.studysync.model.OnlineSession;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/** Tests for the Observer pattern (the Subject) */
class SessionNotifierTest {

    private final SessionEvent event = new SessionEvent(SessionEvent.Type.CREATED,
            new OnlineSession("https://meet.google.com/abc"), "New session");

    @Test
    void notifyObserversCallsEveryObserver() {
        SessionObserver first = mock(SessionObserver.class);
        SessionObserver second = mock(SessionObserver.class);
        SessionNotifier notifier = new SessionNotifier(List.of(first, second));

        notifier.notifyObservers(event);

        verify(first, times(1)).update(event);
        verify(second, times(1)).update(event);
    }

    @Test
    void detachedObserverIsNotCalled() {
        SessionObserver first = mock(SessionObserver.class);
        SessionObserver second = mock(SessionObserver.class);
        SessionNotifier notifier = new SessionNotifier(List.of(first, second));

        notifier.detach(second);
        notifier.notifyObservers(event);

        assertEquals(1, notifier.getObserverCount());
        verify(first).update(event);
        verify(second, never()).update(event);
    }
}