package com.nla.rsvp.entity;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "RSVP_TOKEN")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TOKEN_VALUE", unique = true, nullable = false)
    private String tokenValue;

    @Column(name = "EXPIRATION_DATE", nullable = false)
    private LocalDateTime expiratationDate;

    @Column(name = "EXPIRED", nullable = false)
    private boolean expired;

    @Column(name = "REVOKE_TOKEN", nullable = false)
    private boolean revokeToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
