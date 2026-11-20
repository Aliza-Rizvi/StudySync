package com.studysync.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/** A registered student with profile, courses and free time. Table: students */
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    /** We never save the real password, only its SHA-256 hash */
    @Column(nullable = false)
    private String passwordHash;

    private String sapId;
    private int semester;
    private String department;
    private double cgpa;

    @Enumerated(EnumType.STRING)
    private StudyStyle studyStyle = StudyStyle.DISCUSSION_PHYSICAL;

    /** Courses this student is studying (extra table: student_courses) */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "student_courses",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> courses = new HashSet<>();

    /** Free time slots (read-only here, they are saved through AvailabilityRepository) */
    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER)
    private Set<AvailabilitySlot> availability = new HashSet<>();

    public Student() {
    }

    public Student(String fullName, String email, String passwordHash) {
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    /** true if this student is enrolled in the course with this id */
    public boolean isEnrolledIn(Long courseId) {
        return courses.stream().anyMatch(c -> c.getId().equals(courseId));
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getSapId() { return sapId; }
    public void setSapId(String sapId) { this.sapId = sapId; }
    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public double getCgpa() { return cgpa; }
    public void setCgpa(double cgpa) { this.cgpa = cgpa; }
    public StudyStyle getStudyStyle() { return studyStyle; }
    public void setStudyStyle(StudyStyle studyStyle) { this.studyStyle = studyStyle; }
    public Set<Course> getCourses() { return courses; }
    public void setCourses(Set<Course> courses) { this.courses = courses; }
    public Set<AvailabilitySlot> getAvailability() { return availability; }
    public void setAvailability(Set<AvailabilitySlot> availability) { this.availability = availability; }
}