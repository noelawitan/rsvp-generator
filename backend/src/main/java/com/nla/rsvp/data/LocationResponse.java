package com.nla.rsvp.data;

import com.nla.rsvp.constant.LocationTypes;
import lombok.Data;

import java.time.LocalTime;

@Data
public class LocationResponse {
    private Long id;
    private String name;
    private LocationTypes locationType;
    private String address;
    private String directionUrl;
    private LocalTime startTime;
    private LocalTime endTime;
}
