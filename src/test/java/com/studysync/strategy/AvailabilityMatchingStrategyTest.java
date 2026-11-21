package com.studysync.strategy;

import com.studysync.model.AvailabilitySlot;
import com.studysync.model.Student;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AvailabilityMatchingStrategyTest {

    private final AvailabilityMatchingStrategy strategy = new AvailabilityMatchingStrategy();

    private Student studentFreeAt(DayOfWeek day, int fromHour, int toHour) {
        Student student = new Student("Test", "test@test.com", "hash");
        student.getAvailability().add(new AvailabilitySlot(student, day, LocalTime.of(fromHour, 0), LocalTime.of(toHour, 0)));
        return student;
    }

    @Test
    void overlappingSlotsGiveSharedHours() {
        Student aliza = studentFreeAt(DayOfWeek.MONDAY, 10, 12);
        Student anum = studentFreeAt(DayOfWeek.MONDAY, 11, 13);
        assertEquals(1.0, strategy.score(aliza, anum), 0.001);   // 11:00 to 12:00 = 1 hour
    }

    @Test
    void differentDaysGiveZero() {
        Student aliza = studentFreeAt(DayOfWeek.MONDAY, 10, 12);
        Student mahtab = studentFreeAt(DayOfWeek.TUESDAY, 10, 12);
        assertEquals(0.0, strategy.score(aliza, mahtab), 0.001);
    }
}