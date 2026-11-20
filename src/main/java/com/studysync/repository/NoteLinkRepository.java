package com.studysync.repository;

import com.studysync.model.NoteLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteLinkRepository extends JpaRepository<NoteLink, Long> {

    List<NoteLink> findBySession_IdOrderByAddedAtAsc(Long sessionId);
}