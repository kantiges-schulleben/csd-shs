package com.klnsdr.axon.permissions.service;

import com.klnsdr.axon.permissions.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByInternalName(String name);
}
