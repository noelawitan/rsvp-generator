package com.nla.rsvp.entity;

import com.nla.rsvp.constant.LocationTypes;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

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
    private String publicId;

    @NotNull
    @Column(name = "EVENT_DATE", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "START_TIME", nullable = false)
    private LocalTime startTime;

    @Column(name = "END_TIME")
    private LocalTime endTime;

    @Column(length = 512, name = "EVENT_MESSAGE")
    private String message;

    @Column(name = "INVITATION_RESPONSE_REDIRECT_URL")
    private String invitationResponseRedirectUrl;

    @ToString.Exclude
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event", cascade = CascadeType.ALL)
    private List<Location> locations = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event", cascade = CascadeType.ALL)
    private List<Invitation> invitations = new ArrayList<>();

    @PrePersist
    private void validate() {
        // Validate if there's an existing Location in an event that has a Primary Location
        if (!CollectionUtils.isEmpty(this.locations)) {

            long numOfPrimaryLocation = this.locations.stream().filter(loc -> loc.getLocationType().equals(LocationTypes.PRIMARY)).count();

            if (numOfPrimaryLocation > 1) {
                throw new IllegalArgumentException("Event has already a Primary Location");
            }
        }
    }
}
