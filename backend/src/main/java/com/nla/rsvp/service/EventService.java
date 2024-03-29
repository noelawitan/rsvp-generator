package com.nla.rsvp.service;

import com.nla.rsvp.entity.Event;
import com.nla.rsvp.entity.User;
import com.nla.rsvp.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    public Optional<Event> getById(Long id) {
        return eventRepository.findById(id);
    }

    public Optional<Event> getByPublicId(String publicId) {
        return eventRepository.findByPublicId(publicId);
    }

    public List<Event> getAllByUser(User user) {
        return eventRepository.findByUser(user);
    }
    @Transactional
    public Event save(Event event) {
        return eventRepository.save(event);
    }

    @Transactional
    public void delete(Long id) {
        eventRepository.deleteById(id);
    }
}
