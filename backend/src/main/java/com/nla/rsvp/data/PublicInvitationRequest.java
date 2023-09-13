package com.nla.rsvp.data;

import lombok.Data;

import java.util.List;

@Data
public class PublicInvitationRequest {
    private boolean attending;
    private List<AttendeeRequest> attendees;
}