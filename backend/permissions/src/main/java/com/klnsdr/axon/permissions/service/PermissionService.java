package com.klnsdr.axon.permissions.service;

import com.klnsdr.axon.permissions.WellKnownPermissions;
import com.klnsdr.axon.permissions.entity.Permission;
import com.klnsdr.axon.permissions.entity.UserPermissions;
import com.klnsdr.axon.user.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final UserPermissionsRepository userPermissionsRepository;

    public PermissionService(PermissionRepository permissionRepository, UserPermissionsRepository userPermissionsRepository) {
        this.permissionRepository = permissionRepository;
        this.userPermissionsRepository = userPermissionsRepository;
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

    public boolean hasPermission(UserEntity user, String permissionName) {
        return hasPermission(user.getId(), permissionName);
    }

    public boolean hasPermission(Long userId, String permissionName) {
        return userPermissionsRepository.findByUserId(userId).stream()
                .anyMatch(up -> up.getPermission().getInternalName().equals(getInternalName(permissionName)));
    }

    public List<UserPermissions> getUserPermissions(UserEntity user) {
        return userPermissionsRepository.findByUserId(user.getId());
    }

    public List<UserPermissions> getUserPermissions(Long userId) {
        return userPermissionsRepository.findByUserId(userId);
    }


    public boolean existsDevPermission() {
        return permissionRepository.findByInternalName(WellKnownPermissions.DEVELOPER.getName()).isPresent();
    }

    private String getInternalName(String name) {
        return name.toLowerCase().replace(" ", "_");
    }
}
