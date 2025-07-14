package com.klnsdr.axon.permissions;

import lombok.Getter;

//@Getter
public enum WellKnownPermissions {
    DEVELOPER("developer");

    public final String name;

    public String getName() {
        return name;
    }

    WellKnownPermissions(String name) {
        this.name = name;
    }
}
