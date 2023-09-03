package com.nla.rsvp.data;

import com.nla.rsvp.constant.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String middleName;
    private Gender gender;
    private String email;
    private LocalDate dateOfBirth;
    private String password;
}
