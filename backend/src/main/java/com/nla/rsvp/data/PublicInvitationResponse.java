package com.nla.rsvp.data;

import lombok.Data;

import java.util.List;

@Data
public class PublicInvitationResponse {
    private InvitationResponse invitation;
    private List<AttendeeResponse> attendees;
}