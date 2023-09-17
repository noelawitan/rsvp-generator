package com.nla.rsvp.service;

import com.nla.rsvp.entity.Guest;
import com.nla.rsvp.entity.User;
import com.nla.rsvp.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GuestService {
    @Autowired
    private GuestRepository guestRepository;

    public Optional<Guest> getById(Long id) {
        return guestRepository.findById(id);
    }

    public List<Guest> getAllByUser(User user) {
        return guestRepository.findByUser(user);
    }

    public Guest save(Guest guest) {
        return guestRepository.save(guest);
    }

    public void delete(Long id) {
        guestRepository.deleteById(id);
    }
}
