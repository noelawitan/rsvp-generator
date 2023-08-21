package com.nla.rsvp.repository;

import com.nla.rsvp.entity.Guest;
import com.nla.rsvp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {
    List<Guest> findByUser(User user);
}
