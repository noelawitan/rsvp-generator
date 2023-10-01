package com.nla.rsvp.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum LocationTypes {
    PRIMARY("Primary"), SECONDARY("Secondary");

    private final String value;

    LocationTypes(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static LocationTypes fromValue(String value) {
        for (LocationTypes locationType : LocationTypes.values()) {
            if (locationType.value.equalsIgnoreCase(value)) {
                return locationType;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
