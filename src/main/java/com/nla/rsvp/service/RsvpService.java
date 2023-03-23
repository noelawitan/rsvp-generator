package com.nla.rsvp.service;

import com.nla.rsvp.entity.Rsvp;
import com.nla.rsvp.repository.RsvpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RsvpService {
    @Autowired
    private RsvpRepository rsvpRepository;

    public List<Rsvp> getAllRSVPs() {
        return rsvpRepository.findAll();
    }

    public Optional<Rsvp> getRSVPById(Long id) {
        return rsvpRepository.findById(id);
    }

    public Rsvp saveRSVP(Rsvp rsvp) {
        return rsvpRepository.save(rsvp);
    }

    public void deleteRSVP(Long id) {
        rsvpRepository.deleteById(id);
    }
}