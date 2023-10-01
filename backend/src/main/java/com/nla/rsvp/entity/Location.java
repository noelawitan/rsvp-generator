package com.nla.rsvp.entity;

import com.nla.rsvp.constant.LocationTypes;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "RSVP_LOCATION")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "LOCATION_NAME")
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false)
    private LocationTypes locationType;

    @NotNull
    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "DIRECTION_URL")
    private String directionUrl;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;

    @Column(name = "START_TIME")
    private LocalTime startTime;

    @Column(name = "END_TIME")
    private LocalTime endTime;
}