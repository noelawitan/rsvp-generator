package com.nla.rsvp.data;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class InvitationResponse {
    private Long id;
    private String publicId;
    private EventResponse event;
    private GuestResponse guest;
    private Boolean attending;
    private Integer extraAttendees;
    private LocalDateTime sentDate;
    private LocalDate deadLine;
}
