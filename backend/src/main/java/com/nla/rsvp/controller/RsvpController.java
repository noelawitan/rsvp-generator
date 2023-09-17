package com.nla.rsvp.controller;

import com.nla.rsvp.data.EventRequest;
import com.nla.rsvp.data.EventResponse;
import com.nla.rsvp.entity.Event;
import com.nla.rsvp.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/events")
public class RsvpController extends BaseController {

    @Autowired
    private EventService eventService;

    @GetMapping("/helloWorld")
    public String helloWorld() {
        return "Hello World";
    }

    @GetMapping("")
    public ResponseEntity<List<EventResponse>> getAll() {
        List<Event> events = eventService.getAllByUser(getCurrentUser());

        return ResponseEntity.ok(convertToList(events, EventResponse.class));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getById(@PathVariable("eventId") Long eventId) {
        Optional<Event> eventOptional = eventService.getById(eventId);

        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();

            if (event.getUser().equals(getCurrentUser())) {
                return ResponseEntity.ok(convert(event, EventResponse.class));
            }
        }
        return ResponseEntity.notFound().build();

    }

    @PostMapping
    public ResponseEntity<EventResponse> create(@RequestBody EventRequest eventRequest) {
        Event event = convert(eventRequest, Event.class);

        if (StringUtils.hasText(event.getInvitationResponseRedirectUrl())) {
            if (!isValidUrl(event.getInvitationResponseRedirectUrl())) {
                return ResponseEntity.internalServerError().build();
            }
        }
        event.setUser(getCurrentUser());
        event = eventService.save(event);

        return ResponseEntity.ok(convert(event, EventResponse.class));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventResponse> update(@PathVariable("eventId") Long eventId, @RequestBody EventRequest updatedEvent) {
        Optional<Event> eventOptional = eventService.getById(eventId);

        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();

            if (event.getUser().equals(getCurrentUser())) {
                merge(updatedEvent, event);

                if (StringUtils.hasText(event.getInvitationResponseRedirectUrl())) {
                    if (!isValidUrl(event.getInvitationResponseRedirectUrl())) {
                        return ResponseEntity.internalServerError().build();
                    }
                }

                event = eventService.save(event);
                return ResponseEntity.ok(convert(event, EventResponse.class));
            }
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> delete(@PathVariable("eventId") Long eventId) {
        Optional<Event> eventOptional = eventService.getById(eventId);

        if (eventOptional.isPresent()) {
            final Event event = eventOptional.get();

            if (event.getUser().equals(getCurrentUser())) {
                eventService.delete(event.getId());
                return ResponseEntity.ok().build();
            }
        }

        return ResponseEntity.notFound().build();
    }

    private boolean isValidUrl(String url) {
        boolean isValid = true;

        if (StringUtils.hasText(url)) {
            try {
                new URL(url);
            } catch (MalformedURLException ignored) {
                isValid = false;
            }
        }

        return isValid;
    }
}
