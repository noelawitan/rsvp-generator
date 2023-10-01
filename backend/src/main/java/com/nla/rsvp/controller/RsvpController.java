package com.nla.rsvp.controller;

import com.nla.rsvp.data.EventRequest;
import com.nla.rsvp.data.EventResponse;
import com.nla.rsvp.entity.Event;
import com.nla.rsvp.entity.Location;
import com.nla.rsvp.service.EventService;
import com.nla.rsvp.service.LocationService;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/events")
public class RsvpController extends BaseController {

    @Autowired
    private EventService eventService;

    @Autowired
    private LocationService locationService;

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
        Event event = convert(eventRequest, Event.class, MatchingStrategies.STRICT);

        if (StringUtils.hasText(event.getInvitationResponseRedirectUrl())) {
            if (!isValidUrl(event.getInvitationResponseRedirectUrl())) {
                return ResponseEntity.internalServerError().build();
            }
        }

        bindLocationsToEvent(event);

        event.setPublicId(String.valueOf(UUID.randomUUID()));
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
                List<Location> oldLocations = new ArrayList<>(event.getLocations());

                merge(updatedEvent, event);

                List<Location> newLocations = convertToList(updatedEvent.getLocations(), Location.class, MatchingStrategies.STRICT);

                Set<Long> oldLocationIds = oldLocations.stream().map(Location::getId).collect(Collectors.toSet());
                Set<Long> newLocationIds = newLocations.stream().map(Location::getId).collect(Collectors.toSet());

                for (Long oldId : oldLocationIds) {
                    if (!newLocationIds.contains(oldId)) {
                        locationService.deleteById(oldId);
                    }
                }

                event.getLocations().clear();
                event.getLocations().addAll(newLocations);

                if (StringUtils.hasText(event.getInvitationResponseRedirectUrl())) {
                    if (!isValidUrl(event.getInvitationResponseRedirectUrl())) {
                        return ResponseEntity.internalServerError().build();
                    }
                }

                bindLocationsToEvent(event);

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

    private void bindLocationsToEvent(Event event) {
        List<Location> locations = event.getLocations();

        if (!CollectionUtils.isEmpty(locations)) {
            for (Location location : locations) {
                if (location.getEvent() == null) {
                    location.setEvent(event);
                }
            }
        }
    }
}
