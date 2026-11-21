package com.studysync.factory;

/** The 3 kinds of session the SessionFactory can create. */
public enum SessionType {
    ONLINE("Online"),
    PHYSICAL("Physical"),
    EXAM_REVISION("Exam Revision");

    private final String label;

    SessionType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}