package com.nla.rsvp.service;

import com.nla.rsvp.entity.Attendee;
import com.nla.rsvp.entity.Guest;
import com.nla.rsvp.repository.AttendeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendeeService {

    @Autowired
    private AttendeeRepository attendeeRepository;

    public List<Attendee> getByPrimaryGuest(Guest guest) {
        return attendeeRepository.findByPrimaryGuest(guest);
    }

    public Attendee save(Attendee attendee) {
        return attendeeRepository.save(attendee);
    }

    public void delete(Long id) {
        attendeeRepository.deleteById(id);
    }
}