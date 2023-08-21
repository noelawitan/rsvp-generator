package com.nla.rsvp.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "RSVP_EVENT")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 4, max = 50)
    @Column(name = "NAME", nullable = false)
    private String name;

    @NotNull
    @Column(name = "EVENT_DATE", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "START_TIME", nullable = false)
    private LocalTime startTime;

    @Column(name = "END_TIME")
    private LocalTime endTime;

    @NotNull
    @Column(name = "LOCATION")
    private String location;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE)
    private List<Invitation> invitations = new ArrayList<>();
}
