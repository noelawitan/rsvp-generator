package com.nla.rsvp.controller;

import com.nla.rsvp.service.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invitation")
public class InvitationController {

    @Autowired
    private InvitationService invitationService;

    @GetMapping("/helloWorld")
    public String helloWorld() {
        return "Hello World";
    }
}
