package com.nla.rsvp.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "RSVP_INVITATION")
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Event event;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Guest guest;

    @NotNull
    @Column(nullable = false)
    private Boolean attending;

    @NotNull
    @Column(nullable = false)
    private Integer numberOfGuest;

    @NotNull
    @Column(name = "SEND_DATE", nullable = false)
    private LocalDateTime sentDate;

    @Column(name = "DEADLINE")
    private LocalDateTime deadLine;

    @PrePersist
    public void prePersist() {
        if (attending == null) {
            attending = false;
        }

        if(numberOfGuest == null) {
            numberOfGuest = 0;
        }
    }
}
