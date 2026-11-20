package com.studysync.repository;

import com.studysync.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findBySession_IdOrderByCreatedAtDesc(Long sessionId);

    /** Average stars other members gave to this student (null if no ratings yet) */
    @Query("SELECT AVG(r.stars) FROM Rating r WHERE r.ratedStudent.id = :studentId")
    Double averageForStudent(@Param("studentId") Long studentId);
}