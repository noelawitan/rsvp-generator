package com.nla.rsvp.data;

import lombok.Data;

@Data
public class GuestResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String phoneNumber;
}
