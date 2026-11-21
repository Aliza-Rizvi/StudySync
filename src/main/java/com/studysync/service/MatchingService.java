package com.studysync.service;

import com.studysync.config.ConfigurationManager;
import com.studysync.model.Course;
import com.studysync.model.Student;
import com.studysync.repository.StudentRepository;
import com.studysync.strategy.MatchResult;
import com.studysync.strategy.MatchingStrategy;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Finds study partners. Spring gives us ALL classes that implement MatchingStrategy
 * (availability, grades, style). The student picks one and we use it: that is the Strategy pattern.
 */
@Service
public class MatchingService {

    private final Map<String, MatchingStrategy> strategies = new LinkedHashMap<>();
    private final StudentRepository studentRepository;

    public MatchingService(List<MatchingStrategy> strategyList, StudentRepository studentRepository) {
        for (MatchingStrategy strategy : strategyList) {
            strategies.put(strategy.getKey(), strategy);
        }
        this.studentRepository = studentRepository;
    }

    public List<MatchingStrategy> getStrategies() {
        return new ArrayList<>(strategies.values());
    }

    /** Unknown or empty key = the default strategy from ConfigurationManager (Singleton) */
    public MatchingStrategy getStrategy(String key) {
        MatchingStrategy strategy = (key == null) ? null : strategies.get(key);
        if (strategy == null) {
            strategy = strategies.get(ConfigurationManager.getInstance().getDefaultMatchingStrategy());
        }
        if (strategy == null) {
            throw new IllegalStateException("No matching strategy is available.");
        }
        return strategy;
    }

    /**
     * Best partners for this student, highest score first (top 10).
     * courseId = null means "all my courses".
     */
    public List<MatchResult> findPartners(Long studentId, Long courseId, String strategyKey) {
        Student me = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found."));
        MatchingStrategy strategy = getStrategy(strategyKey);

        // 1. Collect students who study at least one of my courses (no duplicates, not me)
        Map<Long, Student> candidates = new LinkedHashMap<>();
        for (Course course : me.getCourses()) {
            if (courseId != null && !course.getId().equals(courseId)) {
                continue;
            }
            for (Student other : studentRepository.findByCourses_Id(course.getId())) {
                if (!other.getId().equals(me.getId())) {
                    candidates.put(other.getId(), other);
                }
            }
        }

        // 2. Score every candidate with the chosen strategy
        List<MatchResult> results = new ArrayList<>();
        for (Student other : candidates.values()) {
            double score = strategy.score(me, other);
            if (score > 0) {
                results.add(new MatchResult(other, score, sharedCourses(me, other)));
            }
        }

        // 3. Best first, maximum 10
        results.sort(Comparator.comparingDouble(MatchResult::getScore).reversed());
        return results.size() > 10 ? new ArrayList<>(results.subList(0, 10)) : results;
    }

    private List<Course> sharedCourses(Student me, Student other) {
        List<Course> shared = new ArrayList<>();
        for (Course course : me.getCourses()) {
            if (other.isEnrolledIn(course.getId())) {
                shared.add(course);
            }
        }
        return shared;
    }
}