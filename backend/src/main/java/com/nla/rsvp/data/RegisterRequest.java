package com.nla.rsvp.data;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private LocalDate dateOfBirth;
    private String password;
}
