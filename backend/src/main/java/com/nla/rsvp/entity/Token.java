package com.nla.rsvp.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "RSVP_TOKEN")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "TOKEN_VALUE", unique = true, nullable = false)
    private String tokenValue;

    @NotNull
    @Column(name = "EXPIRATION_DATE", nullable = false)
    private LocalDateTime expirationDate;

    @NotNull
    @Column(name = "EXPIRED", nullable = false)
    private Boolean expired;

    @Column(name = "REVOKE_TOKEN", nullable = false)
    private Boolean revokeToken;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
