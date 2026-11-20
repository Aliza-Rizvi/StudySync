package com.studysync.service;

import com.studysync.model.AvailabilitySlot;
import com.studysync.model.Student;
import com.studysync.repository.AvailabilityRepository;
import com.studysync.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvailabilityServiceTest {

    @Mock
    private AvailabilityRepository availabilityRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private AvailabilityService availabilityService;

    @Test
    void addSlotRejectsEndTimeBeforeStartTime() {
        assertThrows(IllegalArgumentException.class,
                () -> availabilityService.addSlot(1L, DayOfWeek.MONDAY, LocalTime.of(15, 0), LocalTime.of(14, 0)));
        verifyNoInteractions(availabilityRepository);
    }

    @Test
    void addSlotSavesValidSlot() {
        Student student = new Student("Aliza Rizvi", "aliza@test.com", "hash");
        student.setId(1L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(availabilityRepository.save(any(AvailabilitySlot.class))).thenAnswer(call -> call.getArgument(0));

        AvailabilitySlot slot = availabilityService.addSlot(1L, DayOfWeek.MONDAY, LocalTime.of(14, 0), LocalTime.of(16, 0));

        assertEquals(DayOfWeek.MONDAY, slot.getDayOfWeek());
        assertSame(student, slot.getStudent());
    }
}