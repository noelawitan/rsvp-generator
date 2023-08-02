package com.nla.rsvp.repository;

import com.nla.rsvp.entity.Event;
import com.nla.rsvp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByUser(User user);
}
