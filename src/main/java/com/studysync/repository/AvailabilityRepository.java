package com.studysync.repository;

import com.studysync.model.AvailabilitySlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<AvailabilitySlot, Long> {

    List<AvailabilitySlot> findByStudent_Id(Long studentId);
}