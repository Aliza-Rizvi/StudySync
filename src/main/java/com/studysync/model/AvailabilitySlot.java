package com.studysync.model;

import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

/** One free time slot of a student, e.g. Monday 14:00 to 17:00. Table: availability_slots */
@Entity
@Table(name = "availability_slots")
public class AvailabilitySlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id")
    private Student student;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    public AvailabilitySlot() {
    }

    public AvailabilitySlot(Student student, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.student = student;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /** "MONDAY" becomes "Monday" for the pages */
    public String getDayLabel() {
        String name = dayOfWeek.name();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(DayOfWeek dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
}