package com.klnsdr.axon.auth.token.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "authTokens")
public class TokenEntity {
    @Id
    @Column(name = "token", columnDefinition = "VARCHAR(402)")
    private String token;

    @Column(name = "valid_till", columnDefinition = "TIMESTAMP")
    private Date validTill;

    public TokenEntity() {
    }

    public TokenEntity(String token, Date validTill) {
        this.token = token;
        this.validTill = validTill;
    }
}