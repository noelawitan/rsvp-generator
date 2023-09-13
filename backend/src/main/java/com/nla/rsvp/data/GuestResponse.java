package com.nla.rsvp.data;

import com.nla.rsvp.constant.Gender;
import lombok.Data;

import java.util.List;

@Data
public class GuestResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private Gender gender;
    private String email;
    private String phoneNumber;
    private List<AttendeeResponse> attendees;
}
