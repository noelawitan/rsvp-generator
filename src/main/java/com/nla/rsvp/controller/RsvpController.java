package com.nla.rsvp.controller;

import com.nla.rsvp.data.EventRequest;
import com.nla.rsvp.data.EventResponse;
import com.nla.rsvp.entity.Event;
import com.nla.rsvp.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class RsvpController extends BasicController {

    @Autowired
    private EventService eventService;

    @GetMapping("/helloWorld")
    public String helloWorld() {
        return "Hello World";
    }

    @GetMapping("")
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        List<Event> events = eventService.getAllEventsOfUser(getCurrentUser());
        return ResponseEntity.ok(convertToList(events, EventResponse.class));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long eventId) {
        Optional<Event> eventOptional = eventService.getEventById(eventId);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            if (event.getUser().equals(getCurrentUser())) {
                return ResponseEntity.ok(convert(event, EventResponse.class));
            }
        }
        return ResponseEntity.notFound().build();

    }

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@RequestBody EventRequest eventRequest) {
        Event event = convert(eventRequest, Event.class);
        event.setUser(getCurrentUser());
        event = eventService.saveEvent(event);

        if (ObjectUtils.isEmpty(event)) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok(convert(event, EventResponse.class));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventResponse> updateEvent(@PathVariable Long eventId, @RequestBody EventRequest updatedEvent) {
        Optional<Event> eventOptional = eventService.getEventById(eventId);

        ResponseEntity<EventResponse> responseEntity = ResponseEntity.notFound().build();

        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            if (event.getUser().equals(getCurrentUser())) {
                merge(updatedEvent, event);

                event = eventService.saveEvent(event);
                if (ObjectUtils.isEmpty(event)) {
                    responseEntity = ResponseEntity.internalServerError().build();
                } else {
                    convert(event, EventResponse.class);
                    responseEntity = ResponseEntity.ok(convert(event, EventResponse.class));
                }
            } else {
                responseEntity = ResponseEntity.notFound().build();
            }
        }

        return responseEntity;
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        Optional<Event> eventOptional = eventService.getEventById(eventId);
        if (eventOptional.isPresent()) {
            final Event event = eventOptional.get();
            if (event.getUser().equals(getCurrentUser())) {
                eventService.deleteEvent(event.getId());
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.internalServerError().build();
    }
}
