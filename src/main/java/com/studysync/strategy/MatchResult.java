package com.studysync.strategy;

import com.studysync.model.Course;
import com.studysync.model.Student;

import java.util.List;

/** One suggested partner with his/her score, shown on the Find Partners page. */
public class MatchResult {

    private final Student student;
    private final double score;
    private final List<Course> sharedCourses;

    public MatchResult(Student student, double score, List<Course> sharedCourses) {
        this.student = student;
        this.score = score;
        this.sharedCourses = sharedCourses;
    }

    public Student getStudent() {
        return student;
    }

    public double getScore() {
        return score;
    }

    /** Score with 1 decimal place, e.g. "2.5" */
    public String getScoreText() {
        return String.format("%.1f", score);
    }

    public List<Course> getSharedCourses() {
        return sharedCourses;
    }
}