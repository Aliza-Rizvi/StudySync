package com.studysync.strategy;

import com.studysync.model.Student;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GradeMatchingStrategyTest {

    private final GradeMatchingStrategy strategy = new GradeMatchingStrategy();

    private Student studentWithCgpa(double cgpa) {
        Student student = new Student("Test", "test@test.com", "hash");
        student.setCgpa(cgpa);
        return student;
    }

    @Test
    void similarCgpaGivesHighScore() {
        assertEquals(3.8, strategy.score(studentWithCgpa(3.5), studentWithCgpa(3.3)), 0.001);
    }

    @Test
    void missingCgpaGivesZero() {
        assertEquals(0.0, strategy.score(studentWithCgpa(3.5), studentWithCgpa(0)), 0.001);
    }
}