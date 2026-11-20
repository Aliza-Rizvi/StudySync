package com.studysync.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/** A join request or an invitation for a group. Table: join_requests */
@Entity
@Table(name = "join_requests")
public class JoinRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "group_id")
    private StudyGroup group;

    /** The student who wants to join, or who was invited */
    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id")
    private Student student;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestType requestType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status = RequestStatus.PENDING;

    private LocalDateTime createdAt = LocalDateTime.now();

    public JoinRequest() {
    }

    public JoinRequest(StudyGroup group, Student student, RequestType requestType) {
        this.group = group;
        this.student = student;
        this.requestType = requestType;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public StudyGroup getGroup() { return group; }
    public void setGroup(StudyGroup group) { this.group = group; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public RequestType getRequestType() { return requestType; }
    public void setRequestType(RequestType requestType) { this.requestType = requestType; }
    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}