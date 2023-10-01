package com.nla.rsvp.data;

import com.nla.rsvp.constant.LocationTypes;
import lombok.Data;

import java.time.LocalTime;

@Data
public class LocationRequest {
    private Long id;
    private LocationTypes locationType;
    private String name;
    private String address;
    private String directionUrl;
    private LocalTime startTime;
    private LocalTime endTime;
}