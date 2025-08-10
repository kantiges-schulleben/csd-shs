package com.klnsdr.axon.permissions;

import com.klnsdr.axon.permissions.entity.Permission;
import com.klnsdr.axon.permissions.service.PermissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PermissionsResourceTest {
    private PermissionService permissionService;
    private PermissionsResource permissionsResource;

    @BeforeEach
    void setup() {
        permissionService = mock(PermissionService.class);
        permissionsResource = new PermissionsResource(permissionService);
    }

    @Test
    void testGetAllPermissions() {
        final Permission READ_PERMISSION = new Permission();
        READ_PERMISSION.setName("READ");
        READ_PERMISSION.setInternalName("read");
        READ_PERMISSION.setId(1L);
        final Permission WRITE_PERMISSION = new Permission();
        WRITE_PERMISSION.setName("WRITE");
        WRITE_PERMISSION.setInternalName("write");
        WRITE_PERMISSION.setId(2L);

        List<Permission> mockPermissions = List.of(READ_PERMISSION, WRITE_PERMISSION);
        when(permissionService.getAllPermissions()).thenReturn(mockPermissions);

        List<Permission> result = permissionsResource.getAllPermissions();

        assertEquals(mockPermissions, result);
        verify(permissionService, times(1)).getAllPermissions();
    }

    @Test
    void testGetPermissionsByUserId() {
        final Permission READ_PERMISSION = new Permission();
        READ_PERMISSION.setName("READ");
        READ_PERMISSION.setInternalName("read");
        READ_PERMISSION.setId(1L);
        Long userId = 42L;
        List<Permission> mockUserPermissions = List.of(READ_PERMISSION);
        when(permissionService.getUserPermissionsList(userId)).thenReturn(mockUserPermissions);

        List<Permission> result = permissionsResource.getPermissions(userId);

        assertEquals(mockUserPermissions, result);
        verify(permissionService, times(1)).getUserPermissionsList(userId);
    }
}
