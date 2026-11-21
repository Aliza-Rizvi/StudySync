package com.studysync.observer;

import com.studysync.model.Notification;
import com.studysync.model.PhysicalSession;
import com.studysync.model.Student;
import com.studysync.model.StudyGroup;
import com.studysync.repository.NotificationRepository;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NotificationObserverTest {

    private Student student(long id) {
        Student student = new Student("Student " + id, id + "@test.com", "hash");
        student.setId(id);
        return student;
    }

    @Test
    void savesOneNotificationForEveryGroupMember() {
        StudyGroup group = new StudyGroup("SCD Prep", "", null, student(1), 6);
        group.getMembers().add(student(1));
        group.getMembers().add(student(2));
        group.getMembers().add(student(3));
        PhysicalSession session = new PhysicalSession("Library");
        session.setGroup(group);

        NotificationRepository repository = mock(NotificationRepository.class);
        NotificationObserver observer = new NotificationObserver(repository);

        observer.update(new SessionEvent(SessionEvent.Type.CREATED, session, "New session"));

        verify(repository, times(3)).save(any(Notification.class));
    }
}