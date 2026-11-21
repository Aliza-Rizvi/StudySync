package com.studysync.strategy;

import com.studysync.model.Student;
import com.studysync.model.StudyStyle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StudyStyleMatchingStrategyTest {

    private final StudyStyleMatchingStrategy strategy = new StudyStyleMatchingStrategy();

    private Student studentWithStyle(StudyStyle style) {
        Student student = new Student("Test", "test@test.com", "hash");
        student.setStudyStyle(style);
        return student;
    }

    @Test
    void sameStyleGivesThreePoints() {
        assertEquals(3.0, strategy.score(studentWithStyle(StudyStyle.QUIET_ONLINE),
                studentWithStyle(StudyStyle.QUIET_ONLINE)), 0.001);
    }

    @Test
    void sameModeDifferentNoiseGivesOneAndHalf() {
        assertEquals(1.5, strategy.score(studentWithStyle(StudyStyle.QUIET_ONLINE),
                studentWithStyle(StudyStyle.DISCUSSION_ONLINE)), 0.001);
    }
}