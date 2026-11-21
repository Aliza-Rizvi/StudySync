package com.studysync.strategy;

import com.studysync.model.AvailabilitySlot;
import com.studysync.model.Student;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalTime;

/** Score = number of free hours per week that both students share. */
@Component
public class AvailabilityMatchingStrategy implements MatchingStrategy {

    @Override
    public String getKey() {
        return "availability";
    }

    @Override
    public String getDisplayName() {
        return "By Availability (shared free time)";
    }

    @Override
    public double score(Student me, Student other) {
        long sharedMinutes = 0;
        for (AvailabilitySlot mine : me.getAvailability()) {
            for (AvailabilitySlot theirs : other.getAvailability()) {
                sharedMinutes += overlapMinutes(mine, theirs);
            }
        }
        return sharedMinutes / 60.0;
    }

    /** Minutes two slots overlap. Different days = 0. */
    long overlapMinutes(AvailabilitySlot a, AvailabilitySlot b) {
        if (a.getDayOfWeek() != b.getDayOfWeek()) {
            return 0;
        }
        LocalTime start = a.getStartTime().isAfter(b.getStartTime()) ? a.getStartTime() : b.getStartTime();
        LocalTime end = a.getEndTime().isBefore(b.getEndTime()) ? a.getEndTime() : b.getEndTime();
        return end.isAfter(start) ? Duration.between(start, end).toMinutes() : 0;
    }
}