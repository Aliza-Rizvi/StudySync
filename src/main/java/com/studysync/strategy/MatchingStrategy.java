package com.studysync.strategy;

import com.studysync.model.Student;

/**
 * STRATEGY PATTERN: one interface, many interchangeable matching algorithms.
 * MatchingService does not care HOW the score is calculated; it just calls score().
 * To add a new way of matching we only add a new class, nothing else changes.
 */
public interface MatchingStrategy {

    /** Short key used in the URL, e.g. "availability" */
    String getKey();

    /** Name shown in the dropdown, e.g. "By Availability" */
    String getDisplayName();

    /** How good a study partner "other" is for "me". 0 = not a match, higher = better. */
    double score(Student me, Student other);
}