package com.nla.rsvp.data;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
public class EventRequest {
    private String name;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
}
