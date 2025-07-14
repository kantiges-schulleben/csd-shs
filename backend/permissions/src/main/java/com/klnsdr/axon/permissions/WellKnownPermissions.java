package com.klnsdr.axon.permissions;

import lombok.Getter;

@Getter
public enum WellKnownPermissions {
    DEVELOPER("developer"),
    SHS_ADMIN("shs_admin");

    public final String name;
    WellKnownPermissions(String name) {
        this.name = name;
    }
}
