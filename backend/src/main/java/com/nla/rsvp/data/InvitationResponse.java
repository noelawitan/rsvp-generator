package com.nla.rsvp.data;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class InvitationResponse {
    private Long id;
    private String publicId;
    private String invitationUrl;
    private EventResponse event;
    private GuestResponse guest;
    private Boolean attending;
    private Integer extraAttendees;
    private LocalDate sentDate;
    private LocalDateTime responseDateTime;
    private LocalDate deadLine;
}
