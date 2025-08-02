package com.klnsdr.axon.user.service;

import com.klnsdr.axon.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByIdpIDEquals(String idpID);
    List<UserEntity> findByNameContainingIgnoreCase(String name);
}
