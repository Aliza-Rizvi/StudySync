package com.studysync.model;

import jakarta.persistence.*;

/** Was this student present in this session? Table: attendance */
@Entity
@Table(name = "attendance",
        uniqueConstraints = @UniqueConstraint(columnNames = {"session_id", "student_id"}))
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "session_id")
    private StudySession session;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id")
    private Student student;

    private boolean present;

    public Attendance() {
    }

    public Attendance(StudySession session, Student student, boolean present) {
        this.session = session;
        this.student = student;
        this.present = present;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public StudySession getSession() { return session; }
    public void setSession(StudySession session) { this.session = session; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public boolean isPresent() { return present; }
    public void setPresent(boolean present) { this.present = present; }
}