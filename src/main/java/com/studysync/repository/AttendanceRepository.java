package com.studysync.repository;

import com.studysync.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findBySession_Id(Long sessionId);

    Optional<Attendance> findBySession_IdAndStudent_Id(Long sessionId, Long studentId);

    /** How many sessions this student attended */
    long countByStudent_IdAndPresentTrue(Long studentId);
}