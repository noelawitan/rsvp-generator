package com.nla.rsvp.entity;

import com.nla.rsvp.constant.Gender;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "RSVP_GUEST")
public class Guest {
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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "GENDER", nullable = false)
    private Gender gender;

    @Email
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @OneToMany(mappedBy = "guest", cascade = CascadeType.REMOVE)
    private List<Invitation> invitations = new ArrayList<>();

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
}
