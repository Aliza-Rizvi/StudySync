package com.studysync.service;

import com.studysync.model.Student;
import com.studysync.model.StudyStyle;
import com.studysync.repository.CourseRepository;
import com.studysync.repository.StudentRepository;
import com.studysync.util.PasswordHasher;
import org.springframework.stereotype.Service;

import java.util.List;

/** Registration, login, profile and course enrollment. */
@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public StudentService(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public Student register(String fullName, String email, String password,
                            String sapId, int semester, String department) {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Name is required.");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Please enter a valid email.");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }
        String cleanEmail = email.trim().toLowerCase();
        if (studentRepository.existsByEmail(cleanEmail)) {
            throw new IllegalArgumentException("An account with this email already exists.");
        }
        Student student = new Student(fullName.trim(), cleanEmail, PasswordHasher.hash(password));
        student.setSapId(sapId);
        student.setSemester(semester);
        student.setDepartment(department);
        return studentRepository.save(student);
    }

    public Student login(String email, String password) {
        if (email == null || password == null) {
            throw new IllegalArgumentException("Wrong email or password.");
        }
        Student student = studentRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Wrong email or password."));
        if (!PasswordHasher.matches(password, student.getPasswordHash())) {
            throw new IllegalArgumentException("Wrong email or password.");
        }
        return student;
    }

    public Student getById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found."));
    }

    public Student updateProfile(Long id, String fullName, int semester, String department,
                                 double cgpa, StudyStyle studyStyle) {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Name is required.");
        }
        if (cgpa < 0 || cgpa > 4) {
            throw new IllegalArgumentException("CGPA must be between 0 and 4.");
        }
        Student student = getById(id);
        student.setFullName(fullName.trim());
        student.setSemester(semester);
        student.setDepartment(department);
        student.setCgpa(cgpa);
        student.setStudyStyle(studyStyle);
        return studentRepository.save(student);
    }

    /** Replaces the student's courses with the selected ones */
    public Student updateCourses(Long studentId, List<Long> courseIds) {
        Student student = getById(studentId);
        student.getCourses().clear();
        if (courseIds != null && !courseIds.isEmpty()) {
            student.getCourses().addAll(courseRepository.findAllById(courseIds));
        }
        return studentRepository.save(student);
    }
}