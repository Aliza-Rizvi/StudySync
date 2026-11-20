package com.studysync.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/** A longer revision session before a quiz, midterm or final. */
@Entity
@DiscriminatorValue("EXAM_REVISION")
public class ExamRevisionSession extends StudySession {

    private String examTopic;

    /** Room or meeting link */
    private String location;

    public ExamRevisionSession() {
    }

    public ExamRevisionSession(String examTopic, String location) {
        this.examTopic = examTopic;
        this.location = location;
    }

    @Override
    public String getTypeLabel() {
        return "Exam Revision";
    }

    @Override
    public String getLocationDetails() {
        return location + " (Topic: " + examTopic + ")";
    }

    public String getExamTopic() { return examTopic; }
    public void setExamTopic(String examTopic) { this.examTopic = examTopic; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}