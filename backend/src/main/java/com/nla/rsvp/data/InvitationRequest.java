package com.nla.rsvp.data;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class InvitationRequest {
    private GuestRequest guest;
    private EventRequest event;
    private Boolean attending;
    private Integer extraAttendees;
    private LocalDateTime sentDate;
    private LocalDate deadLine;
}
