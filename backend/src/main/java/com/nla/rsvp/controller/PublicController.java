package com.nla.rsvp.controller;


import com.nla.rsvp.data.*;
import com.nla.rsvp.entity.Attendee;
import com.nla.rsvp.entity.Event;
import com.nla.rsvp.entity.Guest;
import com.nla.rsvp.entity.Invitation;
import com.nla.rsvp.service.EventService;
import com.nla.rsvp.service.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/public")
public class PublicController extends BaseController {

    @Autowired
    private InvitationService invitationService;

    @Autowired
    private EventService eventService;

    @Value("${domain.name}")
    private String domainName;

    @GetMapping("/invitation/{invitationPublicId}")
    public ResponseEntity<InvitationResponse> getInvitationByPublicId(@PathVariable("invitationPublicId") String publicId) {

        if (StringUtils.hasText(publicId)) {
            Optional<Invitation> invitationOptional = invitationService.getByPublicId(publicId);

            if (invitationOptional.isPresent()) {
                Invitation invitation = invitationOptional.get();

                return ResponseEntity.ok(convert(invitation, InvitationResponse.class));
            }
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/event/{publicId}")
    public ResponseEntity<PublicEventResponse> getEventByPublicId(@PathVariable("publicId") String publicId) {
        Optional<Event> eventOptional = eventService.getByPublicId(publicId);

        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();

            return ResponseEntity.ok(convert(event, PublicEventResponse.class));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/event/{publicId}/search")
    public ResponseEntity<List<PublicInvitationStrictResponse>> searchEventInvitations(@PathVariable("publicId") String publicId,
                                                                                       @RequestParam(value = "keyword") String keyword) {
        Optional<Event> eventOptional = eventService.getByPublicId(publicId);

        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            List<PublicInvitationStrictResponse> strictInvitationsList = new ArrayList<>();

            if (StringUtils.hasText(keyword)) {
                List<Invitation> invitations = invitationService.searchInvitationsByGuestNameAndEvent(keyword, event);

                for (Invitation invitation : invitations) {
                    PublicInvitationStrictResponse publicInvitationStrictResponse = new PublicInvitationStrictResponse();
                    final Guest guest = invitation.getGuest();
                    final String guestName = String.format("%s %s", guest.getFirstName(), guest.getLastName());
                    URI uri = ServletUriComponentsBuilder.fromHttpUrl(domainName)
                            .path("/public/inv/{invitationPublicId}")
                            .buildAndExpand(invitation.getPublicId())
                            .toUri();

                    String invitationUrl = uri.toString();

                    publicInvitationStrictResponse.setGuestName(guestName);
                    publicInvitationStrictResponse.setInvitationUrl(invitationUrl);

                    strictInvitationsList.add(publicInvitationStrictResponse);
                }
            }

            return ResponseEntity.ok(strictInvitationsList);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/invitation/{invitationPublicId}")
    public ResponseEntity<Void> updateInvitationByPublicId(@PathVariable("invitationPublicId") String publicId, @RequestBody PublicInvitationRequest publicInvitationRequest) {
        if (StringUtils.hasText(publicId)) {
            Optional<Invitation> invitationOptional = invitationService.getByPublicId(publicId);

            if (invitationOptional.isPresent()) {
                Invitation invitation = invitationOptional.get();
                invitation.setAttending(publicInvitationRequest.isAttending());
                invitation.setResponseDateTime(LocalDateTime.now());

                if (!CollectionUtils.isEmpty(publicInvitationRequest.getAttendees())) {
                    if (publicInvitationRequest.getAttendees().size() > invitation.getExtraAttendees()) {
                        return ResponseEntity.internalServerError().build();
                    }

                    List<Attendee> guestAttendees = getAttendees(publicInvitationRequest, invitation.getGuest());
                    invitation.getGuest().getAttendees().clear();
                    invitation.getGuest().getAttendees().addAll(guestAttendees);
                } else {
                    invitation.getGuest().getAttendees().clear();
                }

                invitationService.save(invitation);

                return ResponseEntity.ok().build();
            }
        }

        return ResponseEntity.notFound().build();
    }

    private static List<Attendee> getAttendees(PublicInvitationRequest publicInvitationRequest, Guest guest) {
        List<Attendee> guestAttendees = new ArrayList<>();
        for (AttendeeRequest attendeeRequest : publicInvitationRequest.getAttendees()) {
            if (StringUtils.hasText(attendeeRequest.getFirstName()) && StringUtils.hasText(attendeeRequest.getLastName())) {
                Attendee attendee = new Attendee();
                attendee.setFirstName(attendeeRequest.getFirstName());
                attendee.setMiddleName(attendeeRequest.getMiddleName());
                attendee.setLastName(attendeeRequest.getLastName());
                attendee.setPrimaryGuest(guest);

                guestAttendees.add(attendee);
            }
        }
        return guestAttendees;
    }
}