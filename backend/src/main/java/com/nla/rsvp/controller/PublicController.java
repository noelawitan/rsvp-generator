package com.nla.rsvp.controller;


import com.nla.rsvp.data.AttendeeRequest;
import com.nla.rsvp.data.InvitationResponse;
import com.nla.rsvp.data.PublicInvitationRequest;
import com.nla.rsvp.entity.Attendee;
import com.nla.rsvp.entity.Guest;
import com.nla.rsvp.entity.Invitation;
import com.nla.rsvp.service.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/public")
public class PublicController extends BaseController {

    @Autowired
    private InvitationService invitationService;

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

    @PutMapping("/invitation/{invitationPublicId}")
    public ResponseEntity<Void> updateInvitationByPublicId(@PathVariable("invitationPublicId") String publicId,
                                                           @RequestBody PublicInvitationRequest publicInvitationRequest) {
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