package com.studysync.model;

/**
 * How a student likes to study.
 * Two things are combined: quiet or discussion, and online or physical.
 */
public enum StudyStyle {
    QUIET_ONLINE("Quiet - Online", true, true),
    QUIET_PHYSICAL("Quiet - Physical", true, false),
    DISCUSSION_ONLINE("Discussion - Online", false, true),
    DISCUSSION_PHYSICAL("Discussion - Physical", false, false);

    private final String label;
    private final boolean quiet;
    private final boolean online;

    StudyStyle(String label, boolean quiet, boolean online) {
        this.label = label;
        this.quiet = quiet;
        this.online = online;
    }

    public String getLabel() {
        return label;
    }

    public boolean isQuiet() {
        return quiet;
    }

    public boolean isOnline() {
        return online;
    }
}