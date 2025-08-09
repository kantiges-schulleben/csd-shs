package com.klnsdr.axon.permissions.service;

import com.klnsdr.axon.permissions.WellKnownPermissions;
import com.klnsdr.axon.permissions.entity.Permission;
import com.klnsdr.axon.permissions.entity.UserPermissions;
import com.klnsdr.axon.user.entity.UserEntity;
import com.klnsdr.axon.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {
    private static final Logger logger = LoggerFactory.getLogger(PermissionService.class);
    private final PermissionRepository permissionRepository;
    private final UserPermissionsRepository userPermissionsRepository;
    private final UserService userService;

    public PermissionService(PermissionRepository permissionRepository, UserPermissionsRepository userPermissionsRepository, UserService userService) {
        this.permissionRepository = permissionRepository;
        this.userPermissionsRepository = userPermissionsRepository;
        this.userService = userService;
    }

    public Permission createPermission(String name) {
        final Permission permission = new Permission();
        permission.setName(name);
        permission.setInternalName(getInternalName(name));

        if (permissionRepository.findByInternalName(permission.getInternalName()).isPresent()) {
            throw new IllegalArgumentException("Permission with name '" + name + "' already exists.");
        }

        return permissionRepository.save(permission);
    }

    public Optional<Permission> getPermissionByName(String name) {
        return permissionRepository.findByInternalName(name);
    }

    public boolean addPermissionToUser(UserEntity user, Permission permission) {
        final List<UserPermissions> existingPermissions = userPermissionsRepository.findByUserId(user.getId());
        if (existingPermissions.stream().anyMatch(up -> up.getPermission().getId().equals(permission.getId()))) {
            return false;
        }

        final UserPermissions userPermission = new UserPermissions();
        userPermission.setUser(user);
        userPermission.setPermission(permission);
        userPermissionsRepository.save(userPermission);
        return true;
    }

    public boolean hasPermission(Long userId, String permissionName) {
        return userPermissionsRepository.findByUserId(userId).stream()
                .anyMatch(up -> up.getPermission().getInternalName().equals(getInternalName(permissionName)));
    }

    public List<UserPermissions> getUserPermissions(Long userId) {
        return userPermissionsRepository.findByUserId(userId);
    }

    public List<Permission> getUserPermissionsList(Long userId) {
        final List<UserPermissions> permissions = userPermissionsRepository.findByUserId(userId);
        return permissions.stream().map(UserPermissions::getPermission).toList();
    }

    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    public boolean clearAndSetPermissions(Long userId, List<Long> permissionIds) {
        final Optional<UserEntity> user = userService.findById(userId);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + userId + " does not exist.");
        }

        final List<Permission> permissions = new ArrayList<>();

        for (Long permissionId : permissionIds) {
            final Optional<Permission> permission = permissionRepository.findById(permissionId);
            if (permission.isEmpty()) {
                throw new IllegalArgumentException("Permission with ID " + permissionId + " does not exist.");
            }
            permissions.add(permission.get());
        }

        if (!deleteAllUserPermissions(userId)) {
            return false;
        }

        final List<UserPermissions> userPermissions = new ArrayList<>();
        for (Permission permission : permissions) {
            final UserPermissions userPermission = new UserPermissions();
            userPermission.setUser(user.get());
            userPermission.setPermission(permission);
            userPermissions.add(userPermission);
        }

        try {
            userPermissionsRepository.saveAll(userPermissions);
        } catch (Exception e) {
            logger.error("Failed to set permissions for user with ID {}", userId, e);
            return false;
        }

        return true;
    }

    public boolean deleteAllUserPermissions(Long userId) {
        try {
            userPermissionsRepository.deleteByUser_Id(userId);
            return true;
        } catch (Exception e) {
            logger.error("Failed to delete permissions for user with ID {}", userId, e);
            return false;
        }
    }

    public boolean existsDevPermission() {
        return permissionRepository.findByInternalName(WellKnownPermissions.DEVELOPER.getName()).isPresent();
    }

    private String getInternalName(String name) {
        return name.toLowerCase().replace(" ", "_");
    }
}
