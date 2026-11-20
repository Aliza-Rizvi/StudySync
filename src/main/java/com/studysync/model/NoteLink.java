package com.studysync.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/** A link to notes or a resource shared in a session. Table: note_links */
@Entity
@Table(name = "note_links")
public class NoteLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "session_id")
    private StudySession session;

    @ManyToOne(optional = false)
    @JoinColumn(name = "added_by")
    private Student addedBy;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String url;

    private LocalDateTime addedAt = LocalDateTime.now();

    public NoteLink() {
    }

    public NoteLink(StudySession session, Student addedBy, String title, String url) {
        this.session = session;
        this.addedBy = addedBy;
        this.title = title;
        this.url = url;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public StudySession getSession() { return session; }
    public void setSession(StudySession session) { this.session = session; }
    public Student getAddedBy() { return addedBy; }
    public void setAddedBy(Student addedBy) { this.addedBy = addedBy; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public LocalDateTime getAddedAt() { return addedAt; }
    public void setAddedAt(LocalDateTime addedAt) { this.addedAt = addedAt; }
}