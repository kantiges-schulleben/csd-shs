package com.klnsdr.axon.menu.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MenuItemEntity {
    private String name;
    private String location;

    public MenuItemEntity() {
    }

    public MenuItemEntity(String name, String location) {
        this.name = name;
        this.location = location;
    }
}
