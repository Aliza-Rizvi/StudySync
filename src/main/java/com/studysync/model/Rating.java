package com.studysync.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * A rating (1 to 5 stars) given after a session. Table: ratings
 * If ratedStudent is empty (null) the rating is for the session itself.
 */
@Entity
@Table(name = "ratings")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "session_id")
    private StudySession session;

    @ManyToOne(optional = false)
    @JoinColumn(name = "rater_id")
    private Student rater;

    @ManyToOne
    @JoinColumn(name = "rated_student_id")
    private Student ratedStudent;

    private int stars;

    @Column(length = 500)
    private String comment;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Rating() {
    }

    public Rating(StudySession session, Student rater, Student ratedStudent, int stars, String comment) {
        this.session = session;
        this.rater = rater;
        this.ratedStudent = ratedStudent;
        this.stars = stars;
        this.comment = comment;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public StudySession getSession() { return session; }
    public void setSession(StudySession session) { this.session = session; }
    public Student getRater() { return rater; }
    public void setRater(Student rater) { this.rater = rater; }
    public Student getRatedStudent() { return ratedStudent; }
    public void setRatedStudent(Student ratedStudent) { this.ratedStudent = ratedStudent; }
    public int getStars() { return stars; }
    public void setStars(int stars) { this.stars = stars; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}