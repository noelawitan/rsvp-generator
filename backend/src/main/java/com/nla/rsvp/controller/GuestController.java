package com.nla.rsvp.controller;

import com.nla.rsvp.data.GuestRequest;
import com.nla.rsvp.data.GuestResponse;
import com.nla.rsvp.entity.Guest;
import com.nla.rsvp.service.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/guests")
public class GuestController extends BaseController {

    @Autowired
    private GuestService guestService;

    @GetMapping("/helloWorld")
    public String helloWorld() {
        return "Hello World";
    }

    @GetMapping
    public ResponseEntity<List<GuestResponse>> getAll() {
        List<Guest> guests = guestService.getAllByUser(getCurrentUser());

        return ResponseEntity.ok(convertToList(guests, GuestResponse.class));
    }

    @GetMapping("/{guestId}")
    public ResponseEntity<GuestResponse> getById(@PathVariable Long guestId) {
        Optional<Guest> guestOptional = guestService.getById(guestId);

        if (guestOptional.isPresent()) {
            Guest guest = guestOptional.get();

            if (guest.getUser().equals(getCurrentUser())) {
                return ResponseEntity.ok(convert(guest, GuestResponse.class));
            }
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<GuestResponse> createGuest(@RequestBody GuestRequest guestRequest) {
        Guest guest = convert(guestRequest, Guest.class);

        guest.setUser(getCurrentUser());
        guest = guestService.save(guest);

        return ResponseEntity.ok(convert(guest, GuestResponse.class));
    }

    @PutMapping("/{guestId}")
    public ResponseEntity<GuestResponse> updateGuest(@PathVariable Long guestId, @RequestBody GuestRequest guestRequest) {
        Optional<Guest> guestOptional = guestService.getById(guestId);

        if (guestOptional.isPresent()) {
            Guest guest = guestOptional.get();

            if (guest.getUser().equals(getCurrentUser())) {
                merge(guestRequest, guest);

                guest = guestService.save(guest);

                return ResponseEntity.ok(convert(guest, GuestResponse.class));
            }
        }

        return ResponseEntity.internalServerError().build();
    }

    @DeleteMapping("/{guestId}")
    public ResponseEntity<GuestResponse> deleteGuest(@PathVariable Long guestId) {
        Optional<Guest> guestOptional = guestService.getById(guestId);

        if (guestOptional.isPresent()) {
            final Guest guest = guestOptional.get();

            if (guest.getUser().equals(getCurrentUser())) {
                guestService.delete(guest.getId());
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.notFound().build();
    }
}