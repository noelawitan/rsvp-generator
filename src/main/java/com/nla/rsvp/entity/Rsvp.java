package com.nla.rsvp.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "RSVP_MAIN")
public class Rsvp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Invitation invitation;

    private Boolean attending;

    private int numberOfGuests;
}
