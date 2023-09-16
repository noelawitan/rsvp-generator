package com.nla.rsvp.controller;

import com.nla.rsvp.data.EventRequest;
import com.nla.rsvp.data.InvitationRequest;
import com.nla.rsvp.data.InvitationResponse;
import com.nla.rsvp.entity.Event;
import com.nla.rsvp.entity.Guest;
import com.nla.rsvp.entity.Invitation;
import com.nla.rsvp.service.EventService;
import com.nla.rsvp.service.InvitationService;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/invitation")
public class InvitationController extends BaseController {

    @Autowired
    private InvitationService invitationService;

    @Autowired
    private EventService eventService;

    @Value("${domain.name}")
    private String domainName;

    @GetMapping("/{invitationId}")
    public ResponseEntity<InvitationResponse> getInvitation(@PathVariable("invitationId") Long invitationId) {
        Optional<Invitation> invitationOptional = invitationService.getById(invitationId);

        if (invitationOptional.isPresent()) {
            Invitation invitation = invitationOptional.get();

            if (invitation.getUser().equals(getCurrentUser())) {
                InvitationResponse invitationResponse = convert(invitation, InvitationResponse.class);
                URI uri = ServletUriComponentsBuilder.fromHttpUrl(domainName)
                        .path("/public/{invitationPublicId}")
                        .buildAndExpand(invitation.getPublicId())
                        .toUri();

                String invitationUrl = uri.toString();
                invitationResponse.setInvitationUrl(invitationUrl);

                return ResponseEntity.ok(invitationResponse);
            }
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<InvitationResponse>> getEventGuests(
            @PathVariable("eventId") Long eventId) {
        List<Invitation> invitations = invitationService.getByEventId(eventId);

        if (!CollectionUtils.isEmpty(invitations)) {
            if (invitations.get(0).getUser().equals(getCurrentUser())) {
                return ResponseEntity.ok(convertToList(invitations, InvitationResponse.class));
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/event/{eventId}")
    public ResponseEntity<List<InvitationResponse>> createEventGuests(
            @PathVariable("eventId") Long eventId, @RequestBody List<InvitationRequest> invitationRequests) {
        Optional<Event> eventOptional = eventService.getById(eventId);

        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();

            if (event.getUser().equals(getCurrentUser())) {
                if (!CollectionUtils.isEmpty(invitationRequests)) {
                    List<Invitation> invitations = new ArrayList<>();
                    for (InvitationRequest invitationRequest : invitationRequests) {
                        invitationRequest.setEvent(null);

                        Invitation invitation = convert(invitationRequest, Invitation.class, MatchingStrategies.STANDARD);
                        invitation.setEvent(event);
                        invitation.setUser(getCurrentUser());
                        invitation.setPublicId(String.valueOf(UUID.randomUUID()));

                        Guest guest = invitation.getGuest();
                        guest.setUser(getCurrentUser());

                        invitations.add(invitation);
                    }

                    invitations = invitationService.saveAll(invitations);

                    return ResponseEntity.ok(convertToList(invitations, InvitationResponse.class));
                }
            }
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{invitationId}")
    public ResponseEntity<InvitationResponse> updateInvitation(
            @PathVariable("invitationId") Long invitationId, @RequestBody InvitationRequest invitationRequest) {
        Optional<Invitation> invitationOptional = invitationService.getById(invitationId);
        if (invitationOptional.isPresent()) {
            Invitation invitation = invitationOptional.get();

            if (invitation.getUser().equals(getCurrentUser())) {
                EventRequest eventRequest = convert(invitation.getEvent(), EventRequest.class);
                invitationRequest.setEvent(eventRequest);

                merge(invitationRequest, invitation);
                invitation = invitationService.save(invitation);

                return ResponseEntity.ok(convert(invitation, InvitationResponse.class));
            }
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{invitationId}")
    public ResponseEntity<Void> deleteInvitation(@PathVariable("invitationId") Long invitationId) {
        Optional<Invitation> invitationOptional = invitationService.getById(invitationId);

        if (invitationOptional.isPresent()) {
            Invitation invitation = invitationOptional.get();

            if (invitation.getUser().equals(getCurrentUser())) {
                invitationService.delete(invitation.getId());

                return ResponseEntity.ok().build();
            }
        }

        return ResponseEntity.notFound().build();
    }
}