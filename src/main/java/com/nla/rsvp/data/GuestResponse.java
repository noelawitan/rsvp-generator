package com.nla.rsvp.data;

import lombok.Data;

@Data
public class GuestResponse {
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String phoneNumber;
}
