package com.studysync.service;

import com.studysync.factory.SessionFactory;
import com.studysync.factory.SessionType;
import com.studysync.model.*;
import com.studysync.observer.SessionEvent;
import com.studysync.observer.SessionNotifier;
import com.studysync.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/** Schedule sessions, shared notes, attendance, ratings and session history. */
@Service
public class SessionService {

    private static final DateTimeFormatter NICE_TIME = DateTimeFormatter.ofPattern("dd MMM, hh:mm a");

    private final SessionRepository sessionRepository;
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    private final NoteLinkRepository noteLinkRepository;
    private final AttendanceRepository attendanceRepository;
    private final RatingRepository ratingRepository;
    private final SessionFactory sessionFactory;
    private final SessionNotifier sessionNotifier;

    public SessionService(SessionRepository sessionRepository, GroupRepository groupRepository,
                          StudentRepository studentRepository, NoteLinkRepository noteLinkRepository,
                          AttendanceRepository attendanceRepository, RatingRepository ratingRepository,
                          SessionFactory sessionFactory, SessionNotifier sessionNotifier) {
        this.sessionRepository = sessionRepository;
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
        this.noteLinkRepository = noteLinkRepository;
        this.attendanceRepository = attendanceRepository;
        this.ratingRepository = ratingRepository;
        this.sessionFactory = sessionFactory;
        this.sessionNotifier = sessionNotifier;
    }

    /** Uses the FACTORY to build the session and the OBSERVER to tell all members. */
    public StudySession scheduleSession(Long groupId, Long creatorId, SessionType type, String title,
                                        LocalDateTime startTime, int durationMinutes,
                                        String place, String examTopic) {
        StudyGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found."));
        if (!group.hasMember(creatorId)) {
            throw new IllegalArgumentException("Only group members can schedule sessions.");
        }
        if (startTime == null || startTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("The session time must be in the future.");
        }
        Student creator = student(creatorId);

        StudySession session = sessionFactory.createSession(type, title, group, creator,
                startTime, durationMinutes, place, examTopic);
        StudySession saved = sessionRepository.save(session);

        sessionNotifier.notifyObservers(new SessionEvent(SessionEvent.Type.CREATED, saved,
                creator.getFullName() + " scheduled a new " + saved.getTypeLabel() + " session \""
                        + saved.getTitle() + "\" on " + saved.getStartTime().format(NICE_TIME) + "."));
        return saved;
    }

    public StudySession reschedule(Long sessionId, Long actingStudentId, LocalDateTime newStartTime) {
        StudySession session = getSession(sessionId);
        checkMember(session, actingStudentId);
        if (newStartTime == null || newStartTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("The new time must be in the future.");
        }
        session.setStartTime(newStartTime);
        session.setReminderSent(false);
        StudySession saved = sessionRepository.save(session);

        sessionNotifier.notifyObservers(new SessionEvent(SessionEvent.Type.UPDATED, saved,
                "Session \"" + saved.getTitle() + "\" was moved to " + saved.getStartTime().format(NICE_TIME) + "."));
        return saved;
    }

    public StudySession getSession(Long sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found."));
    }

    public List<StudySession> sessionsForGroup(Long groupId) {
        return sessionRepository.findByGroup_IdOrderByStartTimeAsc(groupId);
    }

    /** Sessions that have not finished yet, soonest first */
    public List<StudySession> upcomingForStudent(Long studentId) {
        return allSessionsOf(studentId).stream()
                .filter(session -> !session.isFinished())
                .toList();
    }

    /** Finished sessions, newest first (session history) */
    public List<StudySession> pastForStudent(Long studentId) {
        return allSessionsOf(studentId).stream()
                .filter(StudySession::isFinished)
                .sorted(Comparator.comparing(StudySession::getStartTime).reversed())
                .toList();
    }

    public NoteLink addNoteLink(Long sessionId, Long studentId, String title, String url) {
        StudySession session = getSession(sessionId);
        checkMember(session, studentId);
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Please give the notes a title.");
        }
        if (url == null || !(url.startsWith("http://") || url.startsWith("https://"))) {
            throw new IllegalArgumentException("The link must start with http:// or https://");
        }
        return noteLinkRepository.save(new NoteLink(session, student(studentId), title.trim(), url.trim()));
    }

    public List<NoteLink> notesFor(Long sessionId) {
        return noteLinkRepository.findBySession_IdOrderByAddedAtAsc(sessionId);
    }

    /** Only the student who created the session marks attendance for all members. */
    public void saveAttendance(Long sessionId, Long actingStudentId, Set<Long> presentStudentIds) {
        StudySession session = getSession(sessionId);
        if (!session.getCreatedBy().getId().equals(actingStudentId)) {
            throw new IllegalArgumentException("Only the student who created the session can mark attendance.");
        }
        for (Student member : session.getGroup().getMembers()) {
            Attendance attendance = attendanceRepository.findBySession_IdAndStudent_Id(sessionId, member.getId())
                    .orElse(new Attendance(session, member, false));
            attendance.setPresent(presentStudentIds != null && presentStudentIds.contains(member.getId()));
            attendanceRepository.save(attendance);
        }
    }

    public List<Attendance> attendanceFor(Long sessionId) {
        return attendanceRepository.findBySession_Id(sessionId);
    }

    public long attendanceCount(Long studentId) {
        return attendanceRepository.countByStudent_IdAndPresentTrue(studentId);
    }

    /** ratedStudentId = null means the rating is for the session itself */
    public Rating rate(Long sessionId, Long raterId, Long ratedStudentId, int stars, String comment) {
        StudySession session = getSession(sessionId);
        checkMember(session, raterId);
        if (!session.isFinished()) {
            throw new IllegalArgumentException("You can rate a session only after it ends.");
        }
        if (stars < 1 || stars > 5) {
            throw new IllegalArgumentException("Stars must be between 1 and 5.");
        }
        Student ratedStudent = null;
        if (ratedStudentId != null) {
            if (ratedStudentId.equals(raterId)) {
                throw new IllegalArgumentException("You cannot rate yourself.");
            }
            if (!session.getGroup().hasMember(ratedStudentId)) {
                throw new IllegalArgumentException("You can only rate members of this group.");
            }
            ratedStudent = student(ratedStudentId);
        }
        return ratingRepository.save(new Rating(session, student(raterId), ratedStudent, stars, comment));
    }

    public List<Rating> ratingsFor(Long sessionId) {
        return ratingRepository.findBySession_IdOrderByCreatedAtDesc(sessionId);
    }

    /** Average stars other members gave this student, or null if none yet */
    public Double averageRatingFor(Long studentId) {
        return ratingRepository.averageForStudent(studentId);
    }

    // ---------- helpers ----------

    private List<StudySession> allSessionsOf(Long studentId) {
        List<Long> groupIds = groupRepository.findByMembers_Id(studentId).stream()
                .map(StudyGroup::getId)
                .toList();
        if (groupIds.isEmpty()) {
            return List.of();
        }
        return sessionRepository.findByGroup_IdInOrderByStartTimeAsc(groupIds);
    }

    private void checkMember(StudySession session, Long studentId) {
        if (!session.getGroup().hasMember(studentId)) {
            throw new IllegalArgumentException("Only members of this group can do this.");
        }
    }

    private Student student(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found."));
    }
}