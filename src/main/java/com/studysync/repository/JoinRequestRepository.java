package com.studysync.repository;

import com.studysync.model.JoinRequest;
import com.studysync.model.RequestStatus;
import com.studysync.model.RequestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JoinRequestRepository extends JpaRepository<JoinRequest, Long> {

    /** Requests sent to groups that this student created */
    List<JoinRequest> findByGroup_CreatedBy_IdAndRequestTypeAndStatus(Long creatorId, RequestType type, RequestStatus status);

    /** Invitations (or requests) of this student */
    List<JoinRequest> findByStudent_IdAndRequestTypeAndStatus(Long studentId, RequestType type, RequestStatus status);

    boolean existsByGroup_IdAndStudent_IdAndStatus(Long groupId, Long studentId, RequestStatus status);
}