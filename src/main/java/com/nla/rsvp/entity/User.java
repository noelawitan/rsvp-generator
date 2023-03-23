package com.nla.rsvp.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "RSVP_USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;


    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @Column(name = "MIDDLE_NAME")
    private String middleName;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "DATE_OF_BIRTH", nullable = false)
    private LocalDateTime dateOfBirth;

    //@TODO: Make this password secure and ecnrypted
    @Column(name = "PASSWORD", nullable = false)
    private String password;
}
