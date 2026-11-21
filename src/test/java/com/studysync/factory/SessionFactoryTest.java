package com.studysync.factory;

import com.studysync.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for the Factory pattern */
class SessionFactoryTest {

    private final SessionFactory factory = new SessionFactory();
    private final LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);

    @Test
    void createsOnlineSessionWithLink() {
        StudySession session = factory.createSession(SessionType.ONLINE, "UML Practice", null, null,
                tomorrow, 60, "https://meet.google.com/abc", null);

        assertInstanceOf(OnlineSession.class, session);
        assertEquals("https://meet.google.com/abc", session.getLocationDetails());
        assertEquals("Online", session.getTypeLabel());
    }

    @Test
    void onlineSessionWithoutLinkIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> factory.createSession(SessionType.ONLINE,
                "UML Practice", null, null, tomorrow, 60, "Library", null));
    }

    @Test
    void createsPhysicalSessionWithVenue() {
        StudySession session = factory.createSession(SessionType.PHYSICAL, "Group Study", null, null,
                tomorrow, 90, "Library Room 2", null);

        assertInstanceOf(PhysicalSession.class, session);
        assertEquals(90, session.getDurationMinutes());
    }

    @Test
    void examRevisionGetsDefaultTwoHours() {
        StudySession session = factory.createSession(SessionType.EXAM_REVISION, "Final Revision", null, null,
                tomorrow, 0, "Room 5", "Design Patterns");

        assertInstanceOf(ExamRevisionSession.class, session);
        assertEquals(SessionFactory.EXAM_REVISION_DURATION, session.getDurationMinutes());
    }
}