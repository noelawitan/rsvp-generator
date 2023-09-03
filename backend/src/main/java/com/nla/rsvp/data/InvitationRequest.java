package com.nla.rsvp.data;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InvitationRequest {
    private GuestRequest guest;
    private EventRequest event;
    private Boolean attending;
    private Integer numberOfGuest;
    private LocalDateTime sentDate;
    private LocalDateTime deadLine;
}
