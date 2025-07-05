package com.klnsdr.axon.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @Getter
    private Long id;

    @Column(name = "display_name")
    @Getter
    @Setter
    private String name;

    @Column(name = "idpId")
    @Getter
    @Setter
    private String idpID;
}
