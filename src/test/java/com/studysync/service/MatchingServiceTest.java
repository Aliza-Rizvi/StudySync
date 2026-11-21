package com.studysync.service;

import com.studysync.model.Course;
import com.studysync.model.Student;
import com.studysync.repository.StudentRepository;
import com.studysync.strategy.GradeMatchingStrategy;
import com.studysync.strategy.MatchResult;
import com.studysync.strategy.MatchingStrategy;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MatchingServiceTest {

    private Student student(long id, double cgpa, Course course) {
        Student student = new Student("Student " + id, id + "@test.com", "hash");
        student.setId(id);
        student.setCgpa(cgpa);
        student.getCourses().add(course);
        return student;
    }

    @Test
    void findPartnersSortsBestFirstAndSkipsMe() {
        Course scd = new Course("SE-301", "SCD", "SE");
        scd.setId(10L);
        Student me = student(1, 3.5, scd);
        Student close = student(2, 3.4, scd);   // CGPA very close -> score 3.9
        Student far = student(3, 2.0, scd);     // CGPA far -> score 2.5

        StudentRepository repository = mock(StudentRepository.class);
        when(repository.findById(1L)).thenReturn(Optional.of(me));
        when(repository.findByCourses_Id(10L)).thenReturn(List.of(me, far, close));

        List<MatchingStrategy> strategies = List.of(new GradeMatchingStrategy());
        MatchingService service = new MatchingService(strategies, repository);

        List<MatchResult> results = service.findPartners(1L, null, "grades");

        assertEquals(2, results.size());                       // "me" is not in the list
        assertSame(close, results.get(0).getStudent());        // best match first
        assertSame(far, results.get(1).getStudent());
    }
}