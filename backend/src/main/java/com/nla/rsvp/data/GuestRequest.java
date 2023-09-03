package com.nla.rsvp.data;

import com.nla.rsvp.constant.Gender;
import lombok.Data;

@Data
public class GuestRequest {
    private String firstName;
    private String lastName;
    private String middleName;
    private Gender gender;
    private String email;
    private String phoneNumber;
}
