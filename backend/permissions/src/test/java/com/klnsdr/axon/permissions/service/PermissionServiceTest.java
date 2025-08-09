package com.klnsdr.axon.permissions.service;

import com.klnsdr.axon.permissions.WellKnownPermissions;
import com.klnsdr.axon.permissions.entity.Permission;
import com.klnsdr.axon.permissions.entity.UserPermissions;
import com.klnsdr.axon.user.entity.UserEntity;
import com.klnsdr.axon.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PermissionServiceTest {
    private PermissionService permissionService;
    private PermissionRepository mockPermissionRepository;
    private UserPermissionsRepository mockUserPermissionsRepository;
    private UserService mockUserService;

    @BeforeEach
    public void setUp() {
        mockPermissionRepository = mock(PermissionRepository.class);
        mockUserPermissionsRepository = mock(UserPermissionsRepository.class);
        mockUserService = mock(UserService.class);
        permissionService = new PermissionService(mockPermissionRepository, mockUserPermissionsRepository, mockUserService);
    }

    @Test
    public void createPermission() {
        final String internalName = "test_permission";
        final Permission savedPermission = new Permission();
        savedPermission.setInternalName(internalName);
        savedPermission.setName("test Permission");
        savedPermission.setId(1L);

        when(mockPermissionRepository.findByInternalName(internalName)).thenReturn(Optional.empty());
        when(mockPermissionRepository.save(any())).thenReturn(savedPermission);

        final Permission permission = permissionService.createPermission("test Permission");
        assertNotNull(permission);

        verify(mockPermissionRepository, times(1)).findByInternalName(internalName);
        verify(mockPermissionRepository, times(1)).save(any());
        verify(mockPermissionRepository).save(argThat(p ->
            p.getInternalName().equals(internalName) &&
            p.getName().equals("test Permission") &&
            p.getId() == null
        ));
    }

    @Test
    public void createPermissionFailesWhenInternalNameAlreadyExists() {
        final String internalName = "test_permission";
        when(mockPermissionRepository.findByInternalName(internalName)).thenReturn(Optional.of(new Permission()));

        assertThrows(IllegalArgumentException.class, () -> permissionService.createPermission("test Permission"));

        verify(mockPermissionRepository, times(1)).findByInternalName(internalName);
        verify(mockPermissionRepository, never()).save(any());
    }

    @Test
    public void getPermissionByName() {
        final String internalName = "test_permission";
        final Permission permission = new Permission();
        permission.setInternalName(internalName);
        permission.setName("Test Permission");

        when(mockPermissionRepository.findByInternalName(internalName)).thenReturn(Optional.of(permission));

        final Optional<Permission> result = permissionService.getPermissionByName(internalName);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get());
        verify(mockPermissionRepository, times(1)).findByInternalName(internalName);
    }

    @Test
    public void getPermissionByNameReturnsEmptyWhenNotFound() {
        final String internalName = "non_existent_permission";
        when(mockPermissionRepository.findByInternalName(internalName)).thenReturn(Optional.empty());

        final Optional<Permission> result = permissionService.getPermissionByName(internalName);
        assertNotNull(result);
        assertFalse(result.isPresent());
        verify(mockPermissionRepository, times(1)).findByInternalName(internalName);
    }

    @Test
    public void addPermissionToUser() {
        final UserEntity user = mock(UserEntity.class);
        when(user.getId()).thenReturn(1L);

        final Permission permission = new Permission();
        permission.setId(1L);
        permission.setInternalName("test_permission");
        permission.setName("Test Permission");

        final UserPermissions userPermission = new UserPermissions();
        userPermission.setUser(user);
        userPermission.setPermission(permission);

        when(mockUserPermissionsRepository.findByUserId(1L)).thenReturn(List.of());
        when(mockUserPermissionsRepository.save(any())).thenReturn(userPermission);

        final boolean result = permissionService.addPermissionToUser(user, permission);
        assertTrue(result);

        verify(mockUserPermissionsRepository, times(1)).findByUserId(1L);
        verify(mockUserPermissionsRepository, times(1)).save(argThat(up ->
            up.getUser().equals(user) &&
            up.getPermission().equals(permission)
        ));
    }

    @Test
    public void addPermissionToUserFailsWhenPermissionAlreadyExists() {
        final UserEntity user = mock(UserEntity.class);
        when(user.getId()).thenReturn(1L);

        final Permission permission = new Permission();
        permission.setId(1L);
        permission.setInternalName("test_permission");
        permission.setName("Test Permission");

        final UserPermissions existingPermission = new UserPermissions();
        existingPermission.setUser(user);
        existingPermission.setPermission(permission);

        when(mockUserPermissionsRepository.findByUserId(1L)).thenReturn(List.of(existingPermission));

        final boolean result = permissionService.addPermissionToUser(user, permission);
        assertFalse(result);

        verify(mockUserPermissionsRepository, times(1)).findByUserId(1L);
        verify(mockUserPermissionsRepository, never()).save(any());
    }

    @Test
    public void hasPermissionTrue() {
        final UserEntity user = mock(UserEntity.class);
        when(user.getId()).thenReturn(1L);

        final Permission permission = new Permission();
        permission.setId(1L);
        permission.setInternalName("test_permission");
        permission.setName("Test Permission");

        final UserPermissions existingPermission = new UserPermissions();
        existingPermission.setUser(user);
        existingPermission.setPermission(permission);

        when(mockUserPermissionsRepository.findByUserId(1L)).thenReturn(List.of(existingPermission));
        final boolean hasPermission = permissionService.hasPermission(1L, "test_permission");
        assertTrue(hasPermission);

        verify(mockUserPermissionsRepository, times(1)).findByUserId(1L);
    }

    @Test
    public void hasPermissionFalse() {
        final UserEntity user = mock(UserEntity.class);
        when(user.getId()).thenReturn(1L);

        when(mockUserPermissionsRepository.findByUserId(1L)).thenReturn(List.of());
        final boolean hasPermission = permissionService.hasPermission(1L, "test_permission");
        assertFalse(hasPermission);

        verify(mockUserPermissionsRepository, times(1)).findByUserId(1L);
    }

    @Test
    public void getUserPermissions() {
        when(mockUserPermissionsRepository.findByUserId(1L)).thenReturn(List.of());

        final List<UserPermissions> userPermissions = permissionService.getUserPermissions(1L);

        assertNotNull(userPermissions);
        assertTrue(userPermissions.isEmpty());
        verify(mockUserPermissionsRepository, times(1)).findByUserId(1L);
    }

    @Test
    public void getUserPermissionsList() {
        final UserEntity user = mock(UserEntity.class);
        when(user.getId()).thenReturn(1L);

        final Permission permission = new Permission();
        permission.setId(1L);
        permission.setInternalName("test_permission");
        permission.setName("Test Permission");

        final UserPermissions existingPermission = new UserPermissions();
        existingPermission.setUser(user);
        existingPermission.setPermission(permission);

        when(mockUserPermissionsRepository.findByUserId(1L)).thenReturn(List.of(existingPermission));

        final List<Permission> permissions = permissionService.getUserPermissionsList(1L);
        assertNotNull(permissions);
        assertEquals(1, permissions.size());
        assertEquals(permission, permissions.getFirst());
        verify(mockUserPermissionsRepository, times(1)).findByUserId(1L);
    }

    @Test
    public void getAllPermissions() {
        final Permission permission1 = new Permission();
        permission1.setId(1L);
        permission1.setInternalName("permission_1");
        permission1.setName("Permission 1");

        final Permission permission2 = new Permission();
        permission2.setId(2L);
        permission2.setInternalName("permission_2");
        permission2.setName("Permission 2");

        when(mockPermissionRepository.findAll()).thenReturn(List.of(permission1, permission2));

        final List<Permission> permissions = permissionService.getAllPermissions();
        assertNotNull(permissions);
        assertEquals(2, permissions.size());
        assertTrue(permissions.contains(permission1));
        assertTrue(permissions.contains(permission2));

        verify(mockPermissionRepository, times(1)).findAll();
    }

    @Test
    public void clearAndSetPermissionsSucceedsWithValidUserAndPermissions() {
        final Long userId = 1L;
        final List<Long> permissionIds = List.of(1L, 2L);

        final UserEntity user = mock(UserEntity.class);
        when(user.getId()).thenReturn(userId);

        final Permission permission1 = new Permission();
        permission1.setId(1L);
        permission1.setInternalName("permission_1");
        permission1.setName("Permission 1");

        final Permission permission2 = new Permission();
        permission2.setId(2L);
        permission2.setInternalName("permission_2");
        permission2.setName("Permission 2");

        when(mockUserService.findById(userId)).thenReturn(Optional.of(user));
        when(mockPermissionRepository.findById(1L)).thenReturn(Optional.of(permission1));
        when(mockPermissionRepository.findById(2L)).thenReturn(Optional.of(permission2));
        when(mockUserPermissionsRepository.saveAll(any())).thenReturn(List.of());

        final boolean result = permissionService.clearAndSetPermissions(userId, permissionIds);
        assertTrue(result);

        verify(mockUserService, times(1)).findById(userId);
        verify(mockPermissionRepository, times(1)).findById(1L);
        verify(mockPermissionRepository, times(1)).findById(2L);
        verify(mockUserPermissionsRepository, times(1)).deleteByUser_Id(userId);
        verify(mockUserPermissionsRepository, times(1)).saveAll(any());
    }

    @Test
    public void clearAndSetPermissionsFailsWhenUserDoesNotExist() {
        final Long userId = 1L;
        final List<Long> permissionIds = List.of(1L, 2L);

        when(mockUserService.findById(userId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> permissionService.clearAndSetPermissions(userId, permissionIds));

        verify(mockUserService, times(1)).findById(userId);
        verify(mockPermissionRepository, never()).findById(any());
        verify(mockUserPermissionsRepository, never()).deleteByUser_Id(any());
        verify(mockUserPermissionsRepository, never()).saveAll(any());
    }

    @Test
    public void clearAndSetPermissionsFailsWhenPermissionDoesNotExist() {
        final Long userId = 1L;
        final List<Long> permissionIds = List.of(1L, 2L);

        final UserEntity user = mock(UserEntity.class);
        when(user.getId()).thenReturn(userId);

        when(mockUserService.findById(userId)).thenReturn(Optional.of(user));
        when(mockPermissionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> permissionService.clearAndSetPermissions(userId, permissionIds));

        verify(mockUserService, times(1)).findById(userId);
        verify(mockPermissionRepository, times(1)).findById(1L);
        verify(mockPermissionRepository, never()).findById(2L);
        verify(mockUserPermissionsRepository, never()).deleteByUser_Id(any());
        verify(mockUserPermissionsRepository, never()).saveAll(any());
    }

    @Test
    public void clearAndSetPermissionsFailsWhenDeletePermissionsFails() {
        final Long userId = 1L;
        final List<Long> permissionIds = List.of(1L, 2L);

        final UserEntity user = mock(UserEntity.class);
        when(user.getId()).thenReturn(userId);

        final Permission permission1 = new Permission();
        permission1.setId(1L);
        permission1.setInternalName("permission_1");
        permission1.setName("Permission 1");

        final Permission permission2 = new Permission();
        permission2.setId(2L);
        permission2.setInternalName("permission_2");
        permission2.setName("Permission 2");

        when(mockUserService.findById(userId)).thenReturn(Optional.of(user));
        when(mockPermissionRepository.findById(1L)).thenReturn(Optional.of(permission1));
        when(mockPermissionRepository.findById(2L)).thenReturn(Optional.of(permission2));
        doThrow(new RuntimeException("Database error")).when(mockUserPermissionsRepository).deleteByUser_Id(1L);

        final boolean result = permissionService.clearAndSetPermissions(userId, permissionIds);
        assertFalse(result);

        verify(mockUserService, times(1)).findById(userId);
        verify(mockPermissionRepository, times(1)).findById(1L);
        verify(mockPermissionRepository, times(1)).findById(2L);
        verify(mockUserPermissionsRepository, times(1)).deleteByUser_Id(userId);
        verify(mockUserPermissionsRepository, never()).saveAll(any());
    }

    @Test
    public void clearAndSetPermissionsFailsWhenSavePermissionsThrowsException() {
        final Long userId = 1L;
        final List<Long> permissionIds = List.of(1L, 2L);

        final UserEntity user = mock(UserEntity.class);
        when(user.getId()).thenReturn(userId);

        final Permission permission1 = new Permission();
        permission1.setId(1L);
        permission1.setInternalName("permission_1");
        permission1.setName("Permission 1");

        final Permission permission2 = new Permission();
        permission2.setId(2L);
        permission2.setInternalName("permission_2");
        permission2.setName("Permission 2");

        when(mockUserService.findById(userId)).thenReturn(Optional.of(user));
        when(mockPermissionRepository.findById(1L)).thenReturn(Optional.of(permission1));
        when(mockPermissionRepository.findById(2L)).thenReturn(Optional.of(permission2));
        when(mockUserPermissionsRepository.saveAll(any())).thenThrow(new RuntimeException("Database error"));

        final boolean result = permissionService.clearAndSetPermissions(userId, permissionIds);
        assertFalse(result);

        verify(mockUserService, times(1)).findById(userId);
        verify(mockPermissionRepository, times(1)).findById(1L);
        verify(mockPermissionRepository, times(1)).findById(2L);
        verify(mockUserPermissionsRepository, times(1)).deleteByUser_Id(userId);
        verify(mockUserPermissionsRepository, times(1)).saveAll(any());
    }

    @Test
    public void deleteAllUserPermissions() {
        final Long userId = 1L;

        doNothing().when(mockUserPermissionsRepository).deleteByUser_Id(userId);

        final boolean result = permissionService.deleteAllUserPermissions(userId);
        assertTrue(result);

        verify(mockUserPermissionsRepository, times(1)).deleteByUser_Id(userId);
    }

    @Test
    public void deleteAllUserPermissionsFails() {
        final Long userId = 1L;

        doThrow(new RuntimeException("Database error")).when(mockUserPermissionsRepository).deleteByUser_Id(userId);

        final boolean result = permissionService.deleteAllUserPermissions(userId);
        assertFalse(result);

        verify(mockUserPermissionsRepository, times(1)).deleteByUser_Id(userId);
    }

    @Test
    public void existsDevPermission() {
        final Permission devPermission = new Permission();
        devPermission.setInternalName(WellKnownPermissions.DEVELOPER.getName());

        when(mockPermissionRepository.findByInternalName(WellKnownPermissions.DEVELOPER.getName())).thenReturn(Optional.of(devPermission));

        final boolean exists = permissionService.existsDevPermission();
        assertTrue(exists);

        verify(mockPermissionRepository, times(1)).findByInternalName(WellKnownPermissions.DEVELOPER.getName());
    }

    @Test
    public void existsDevPermissionReturnsFalseWhenNotFound() {
        when(mockPermissionRepository.findByInternalName(WellKnownPermissions.DEVELOPER.getName())).thenReturn(Optional.empty());

        final boolean exists = permissionService.existsDevPermission();
        assertFalse(exists);

        verify(mockPermissionRepository, times(1)).findByInternalName(WellKnownPermissions.DEVELOPER.getName());
    }
}
