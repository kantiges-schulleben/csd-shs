package com.klnsdr.axon.permissions;

import com.klnsdr.axon.permissions.entity.Permission;
import com.klnsdr.axon.permissions.service.PermissionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
public class PermissionsResource {
    private final PermissionService permissionService;

    public PermissionsResource(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping("/")
    public List<Permission> getAllPermissions() {
        return permissionService.getAllPermissions();
    }

    @GetMapping("/user/{ID}")
    public List<Permission> getPermissions(@PathVariable("ID") Long ID) {
        return permissionService.getUserPermissionsList(ID);
    }
}
