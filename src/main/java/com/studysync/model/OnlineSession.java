package com.studysync.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/** A session on Zoom / Google Meet / Teams. */
@Entity
@DiscriminatorValue("ONLINE")
public class OnlineSession extends StudySession {

    private String meetingLink;

    public OnlineSession() {
    }

    public OnlineSession(String meetingLink) {
        this.meetingLink = meetingLink;
    }

    @Override
    public String getTypeLabel() {
        return "Online";
    }

    @Override
    public String getLocationDetails() {
        return meetingLink;
    }

    public String getMeetingLink() { return meetingLink; }
    public void setMeetingLink(String meetingLink) { this.meetingLink = meetingLink; }
}