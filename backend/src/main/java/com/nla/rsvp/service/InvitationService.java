package com.nla.rsvp.service;

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

    public List<Invitation> getAllInvitations() {
        return invitationRepository.findAll();
    }

    public Optional<Invitation> getInvitationById(Long id) {
        return invitationRepository.findById(id);
    }

    public Invitation saveInvitation(Invitation invitation) {
        return invitationRepository.save(invitation);
    }

    public void deleteInvitation(Long id) {
        invitationRepository.deleteById(id);
    }
}
