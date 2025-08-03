package com.klnsdr.axon.shs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "shs_config")
public class ShsConfigEntity {
    @Id
    @Column(name = "id")
    private String key;

    @Column(name = "value")
    private String value;
}
