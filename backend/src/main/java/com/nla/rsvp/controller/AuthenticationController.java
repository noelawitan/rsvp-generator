package com.nla.rsvp.controller;

import com.nla.rsvp.data.AuthenticationRequest;
import com.nla.rsvp.data.AuthenticationResponse;
import com.nla.rsvp.data.RegisterRequest;
import com.nla.rsvp.entity.User;
import com.nla.rsvp.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController extends BaseController {

    @Autowired
    private AuthenticationService authenticateService;

    @GetMapping("/helloWorld")
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok("Hello World");
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody RegisterRequest registerRequest) {
        User user = convert(registerRequest, User.class);
        return ResponseEntity.ok(authenticateService.register(user));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticateService.authenticate(request));
    }

}
