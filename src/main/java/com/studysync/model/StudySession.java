package com.studysync.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * A study session of a group. This class is ABSTRACT: the real objects are
 * OnlineSession, PhysicalSession or ExamRevisionSession (Anum's SessionFactory creates them).
 * All three are saved in ONE table "study_sessions"; the column "session_type" tells which one it is.
 */
@Entity
@Table(name = "study_sessions")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "session_type")
public abstract class StudySession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(optional = false)
    @JoinColumn(name = "group_id")
    private StudyGroup group;

    @ManyToOne(optional = false)
    @JoinColumn(name = "created_by")
    private Student createdBy;

    @Column(nullable = false)
    private LocalDateTime startTime;

    private int durationMinutes = 60;

    /** true after Mahtab's ReminderScheduler has sent the reminder */
    private boolean reminderSent = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    /** "Online", "Physical" or "Exam Revision" */
    public abstract String getTypeLabel();

    /** Meeting link or venue, shown on the pages */
    public abstract String getLocationDetails();

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(durationMinutes);
    }

    public boolean isFinished() {
        return getEndTime().isBefore(LocalDateTime.now());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public StudyGroup getGroup() { return group; }
    public void setGroup(StudyGroup group) { this.group = group; }
    public Student getCreatedBy() { return createdBy; }
    public void setCreatedBy(Student createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
    public boolean isReminderSent() { return reminderSent; }
    public void setReminderSent(boolean reminderSent) { this.reminderSent = reminderSent; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}