package com.nla.rsvp.repository;

import com.nla.rsvp.entity.Rsvp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RsvpRepository extends JpaRepository<Rsvp, Long> {
}
