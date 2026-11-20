package com.studysync.service;

import com.studysync.model.Student;
import com.studysync.repository.CourseRepository;
import com.studysync.repository.StudentRepository;
import com.studysync.util.PasswordHasher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/** Mockito gives us a FAKE StudentRepository, so these tests do not need MySQL. */
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void registerSavesStudentWithHashedPassword() {
        when(studentRepository.existsByEmail("aliza@test.com")).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenAnswer(call -> call.getArgument(0));

        Student saved = studentService.register("Aliza Rizvi", "Aliza@Test.com", "secret123", "58782", 6, "SE");

        assertEquals("aliza@test.com", saved.getEmail());
        assertNotEquals("secret123", saved.getPasswordHash());
        assertTrue(PasswordHasher.matches("secret123", saved.getPasswordHash()));
    }

    @Test
    void registerRejectsDuplicateEmail() {
        when(studentRepository.existsByEmail("aliza@test.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> studentService.register("Aliza Rizvi", "aliza@test.com", "secret123", "58782", 6, "SE"));
        verify(studentRepository, never()).save(any());
    }

    @Test
    void loginWorksWithCorrectPassword() {
        Student student = new Student("Aliza Rizvi", "aliza@test.com", PasswordHasher.hash("secret123"));
        when(studentRepository.findByEmail("aliza@test.com")).thenReturn(Optional.of(student));

        assertSame(student, studentService.login("aliza@test.com", "secret123"));
    }

    @Test
    void loginFailsWithWrongPassword() {
        Student student = new Student("Aliza Rizvi", "aliza@test.com", PasswordHasher.hash("secret123"));
        when(studentRepository.findByEmail("aliza@test.com")).thenReturn(Optional.of(student));

        assertThrows(IllegalArgumentException.class, () -> studentService.login("aliza@test.com", "wrongpass"));
    }
}