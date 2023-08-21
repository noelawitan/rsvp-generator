package com.nla.rsvp.data;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String email;
    private String password;
}
