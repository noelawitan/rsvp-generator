package com.nla.rsvp.repository;

import com.nla.rsvp.entity.Event;
import com.nla.rsvp.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    Optional<Invitation> findByPublicId(String publicId);

    List<Invitation> findByEventId(Long eventId);

    @Query("SELECT i FROM Invitation i JOIN i.guest g WHERE CONCAT(LOWER(g.firstName), ' ', LOWER(g.lastName)) LIKE LOWER(CONCAT('%', :searchGuestName, '%')) AND i.event = :event")
    List<Invitation> searchByGuestNameAndEvent(@Param("searchGuestName") String searchGuestName, @Param("event") Event event);
}
