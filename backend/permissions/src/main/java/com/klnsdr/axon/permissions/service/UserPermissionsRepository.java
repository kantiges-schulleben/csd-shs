package com.klnsdr.axon.permissions.service;

import com.klnsdr.axon.permissions.entity.UserPermissions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPermissionsRepository extends JpaRepository<UserPermissions, Long> {
    List<UserPermissions> findByUserId(Long userId);
}
