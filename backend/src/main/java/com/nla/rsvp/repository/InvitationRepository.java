package com.nla.rsvp.repository;

import com.nla.rsvp.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    Optional<Invitation> findByPublicId(String publicId);

    List<Invitation> findByEventId(Long eventId);
}
