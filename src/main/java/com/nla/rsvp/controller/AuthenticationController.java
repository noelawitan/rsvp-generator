package com.nla.rsvp.controller;

import com.nla.rsvp.data.AuthenticationRequest;
import com.nla.rsvp.data.AuthenticationResponse;
import com.nla.rsvp.data.RegisterRequest;
import com.nla.rsvp.service.AuthenticationService;
import com.nla.rsvp.service.JwtService;
import com.nla.rsvp.service.TokenService;
import com.nla.rsvp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticateService;

    @GetMapping("/helloWorld")
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok("Hello World");
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authenticateService.register(registerRequest));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticateService.authenticate(request));
    }

}
