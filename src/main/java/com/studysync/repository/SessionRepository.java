package com.studysync.repository;

import com.studysync.model.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<StudySession, Long> {

    List<StudySession> findByGroup_IdOrderByStartTimeAsc(Long groupId);

    /** Sessions of many groups at once (all groups of one student) */
    List<StudySession> findByGroup_IdInOrderByStartTimeAsc(Collection<Long> groupIds);

    /** Sessions starting soon that did not get a reminder yet */
    List<StudySession> findByReminderSentFalseAndStartTimeBetween(LocalDateTime from, LocalDateTime to);
}