package com.studysync.strategy;

import com.studysync.model.Student;
import com.studysync.model.StudyStyle;
import org.springframework.stereotype.Component;

/**
 * Same study style = 3 points.
 * Otherwise: same mode (online/physical) = 1.5 points, same noise level (quiet/discussion) = 1 point.
 */
@Component
public class StudyStyleMatchingStrategy implements MatchingStrategy {

    @Override
    public String getKey() {
        return "style";
    }

    @Override
    public String getDisplayName() {
        return "By Study Style (online/physical, quiet/discussion)";
    }

    @Override
    public double score(Student me, Student other) {
        StudyStyle mine = me.getStudyStyle();
        StudyStyle theirs = other.getStudyStyle();
        if (mine == null || theirs == null) {
            return 0;
        }
        if (mine == theirs) {
            return 3;
        }
        double score = 0;
        if (mine.isOnline() == theirs.isOnline()) {
            score += 1.5;
        }
        if (mine.isQuiet() == theirs.isQuiet()) {
            score += 1;
        }
        return score;
    }
}