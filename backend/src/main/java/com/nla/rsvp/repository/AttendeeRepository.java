package com.nla.rsvp.repository;

import com.nla.rsvp.entity.Attendee;
import com.nla.rsvp.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendeeRepository extends JpaRepository<Attendee, Long> {
    List<Attendee> findByPrimaryGuest(Guest guest);
}