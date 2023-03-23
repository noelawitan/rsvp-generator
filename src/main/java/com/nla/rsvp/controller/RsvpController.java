package com.nla.rsvp.controller;

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
public class RsvpController {

    @Autowired
    private EventService eventService;

    @GetMapping("/helloWorld")
    public String helloWorld() {
        return "Hello World";
    }

    @GetMapping("")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEventById(@PathVariable Long eventId) {
        Optional<Event> eventOptional = eventService.getEventById(eventId);
        return eventOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        event = eventService.saveEvent(event);
        if(ObjectUtils.isEmpty(event)) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(event);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long eventId, @RequestBody Event updatedEvent) {
        Optional<Event> eventOptional = eventService.getEventById(eventId);
        ResponseEntity<Event> responseEntity = ResponseEntity.notFound().build();
        if(eventOptional.isPresent()) {
            Event event = eventOptional.get();
            event.setName(updatedEvent.getName());
            event.setDate(updatedEvent.getDate());
            event.setStartTime(updatedEvent.getStartTime());
            event.setEndTime(updatedEvent.getEndTime());
            event.setLocation(updatedEvent.getLocation());
            event.setInvitations(updatedEvent.getInvitations());

            event = eventService.saveEvent(event);
            if(ObjectUtils.isEmpty(event)) {
                responseEntity = ResponseEntity.internalServerError().build();
            } else {
                responseEntity = ResponseEntity.ok(event);
            }
        }

        return responseEntity;
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        Optional<Event> eventOptional = eventService.getEventById(eventId);
        if(eventOptional.isPresent()) {
            final Event event = eventOptional.get();
            eventService.deleteEvent(event.getId());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.internalServerError().build();
    }
}
