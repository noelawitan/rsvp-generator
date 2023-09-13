package com.nla.rsvp.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "RSVP_ATTENDEE")
public class Attendee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 50)
    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @NotNull
    @Size(min = 2, max = 50)
    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @Size(max = 50)
    @Column(name = "MIDDLE_NAME")
    private String middleName;

    @ManyToOne
    @JoinColumn(name = "guest_id")
    private Guest primaryGuest;
}
