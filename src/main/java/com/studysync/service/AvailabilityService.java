package com.studysync.service;

import com.studysync.model.AvailabilitySlot;
import com.studysync.model.Student;
import com.studysync.repository.AvailabilityRepository;
import com.studysync.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

/** Adds, lists and removes free time slots (the availability calendar). */
@Service
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final StudentRepository studentRepository;

    public AvailabilityService(AvailabilityRepository availabilityRepository, StudentRepository studentRepository) {
        this.availabilityRepository = availabilityRepository;
        this.studentRepository = studentRepository;
    }

    public AvailabilitySlot addSlot(Long studentId, DayOfWeek day, LocalTime start, LocalTime end) {
        if (day == null || start == null || end == null) {
            throw new IllegalArgumentException("Please choose a day, start time and end time.");
        }
        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("End time must be after start time.");
        }
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found."));
        return availabilityRepository.save(new AvailabilitySlot(student, day, start, end));
    }

    /** Slots sorted Monday to Sunday, then by start time */
    public List<AvailabilitySlot> getSlots(Long studentId) {
        return availabilityRepository.findByStudent_Id(studentId).stream()
                .sorted(Comparator.comparing(AvailabilitySlot::getDayOfWeek)
                        .thenComparing(AvailabilitySlot::getStartTime))
                .toList();
    }

    public void removeSlot(Long slotId, Long studentId) {
        AvailabilitySlot slot = availabilityRepository.findById(slotId)
                .orElseThrow(() -> new IllegalArgumentException("Time slot not found."));
        if (!slot.getStudent().getId().equals(studentId)) {
            throw new IllegalArgumentException("You can only remove your own time slots.");
        }
        availabilityRepository.delete(slot);
    }
}