package com.studysync.strategy;

import com.studysync.model.Student;
import org.springframework.stereotype.Component;

/** Students with a similar CGPA study at a similar pace. Score = 4 - CGPA difference. */
@Component
public class GradeMatchingStrategy implements MatchingStrategy {

    @Override
    public String getKey() {
        return "grades";
    }

    @Override
    public String getDisplayName() {
        return "By Grades (similar CGPA)";
    }

    @Override
    public double score(Student me, Student other) {
        if (me.getCgpa() <= 0 || other.getCgpa() <= 0) {
            return 0;   // CGPA not filled in the profile yet
        }
        double difference = Math.abs(me.getCgpa() - other.getCgpa());
        return Math.max(0, 4.0 - difference);
    }
}