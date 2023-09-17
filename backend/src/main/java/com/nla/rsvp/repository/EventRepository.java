package com.nla.rsvp.repository;

import com.nla.rsvp.entity.Event;
import com.nla.rsvp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByPublicId(String publicId);

    List<Event> findByUser(User user);
}
