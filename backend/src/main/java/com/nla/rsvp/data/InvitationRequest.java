package com.nla.rsvp.data;

import lombok.Data;

import java.time.LocalDate;

@Data
public class InvitationRequest {
    private GuestRequest guest;
    private EventRequest event;
    private Boolean attending;
    private Integer extraAttendees;
    private LocalDate sentDate;
    private LocalDate deadLine;
}
