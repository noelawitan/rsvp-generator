package com.nla.rsvp.service;

import com.nla.rsvp.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;

    @Transactional
    public void deleteById(Long id) {
        locationRepository.deleteById(id);
    }
}
