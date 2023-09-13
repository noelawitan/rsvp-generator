package com.nla.rsvp.data;

import lombok.Data;

@Data
public class AttendeeResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
}