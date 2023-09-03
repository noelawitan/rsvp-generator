package com.nla.rsvp.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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
    private String publicId;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Event event;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Guest guest;

    private Boolean attending;

    private Integer numberOfGuest;

    @Column(name = "SEND_DATE")
    private LocalDateTime sentDate;

    @Column(name = "DEADLINE")
    private LocalDateTime deadLine;

    @ToString.Exclude
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
