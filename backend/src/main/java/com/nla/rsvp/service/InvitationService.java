package com.nla.rsvp.service;

import com.nla.rsvp.entity.Event;
import com.nla.rsvp.entity.Invitation;
import com.nla.rsvp.repository.InvitationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvitationService {
    @Autowired
    private InvitationRepository invitationRepository;

    public Optional<Invitation> getById(Long id) {
        return invitationRepository.findById(id);
    }

    public Optional<Invitation> getByPublicId(String publicId) {
        return invitationRepository.findByPublicId(publicId);
    }

    public List<Invitation> getAll() {
        return invitationRepository.findAll();
    }

    public List<Invitation> getByEventId(Long eventId) {
        return invitationRepository.findByEventId(eventId);
    }

    public List<Invitation> searchInvitationsByGuestNameAndEvent(String searchGuestName, Event event) {
        return invitationRepository.searchByGuestNameAndEvent(searchGuestName, event);
    }

    public Invitation save(Invitation invitation) {
        return invitationRepository.save(invitation);
    }

    public List<Invitation> saveAll(List<Invitation> invitations) {
        return invitationRepository.saveAll(invitations);
    }

    public void delete(Long id) {
        invitationRepository.deleteById(id);
    }
}