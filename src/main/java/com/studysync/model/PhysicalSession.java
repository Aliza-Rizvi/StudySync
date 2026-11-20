package com.studysync.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/** A face-to-face session, e.g. in the library. */
@Entity
@DiscriminatorValue("PHYSICAL")
public class PhysicalSession extends StudySession {

    private String venue;

    public PhysicalSession() {
    }

    public PhysicalSession(String venue) {
        this.venue = venue;
    }

    @Override
    public String getTypeLabel() {
        return "Physical";
    }

    @Override
    public String getLocationDetails() {
        return venue;
    }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
}