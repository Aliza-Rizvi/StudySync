package com.studysync.repository;

import com.studysync.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * REPOSITORY PATTERN: the rest of the app asks this interface for students
 * and never writes SQL itself. Spring writes the SQL from the method names.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);

    boolean existsByEmail(String email);

    /** All students enrolled in one course */
    List<Student> findByCourses_Id(Long courseId);
}