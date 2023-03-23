package com.nla.rsvp.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "RSVP_INVITATION")
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Event event;

    @ManyToOne
    private Guest guest;

    @Column(name = "SEND_DATE", nullable = false)
    private LocalDateTime sentDate;

    @Column(name = "DEADLINE")
    private LocalDateTime deadLine;
}
