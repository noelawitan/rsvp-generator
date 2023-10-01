package com.nla.rsvp.data;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class PublicEventResponse {
    private String name;
    private String publicId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String message;
    private List<LocationResponse> locations;
    private String location;
}