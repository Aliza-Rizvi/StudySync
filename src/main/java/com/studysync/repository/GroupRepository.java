package com.studysync.repository;

import com.studysync.model.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<StudyGroup, Long> {

    /** Groups where this student is a member */
    List<StudyGroup> findByMembers_Id(Long studentId);

    /** All groups of one course */
    List<StudyGroup> findByCourse_Id(Long courseId);
}