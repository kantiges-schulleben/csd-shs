package com.klnsdr.axon.auth.token.service;

import com.klnsdr.axon.auth.token.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface TokenRepository extends JpaRepository<TokenEntity, String> {
    void deleteByValidTillBefore(Date now);
}
