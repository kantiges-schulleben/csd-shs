package com.klnsdr.axon;

import com.klnsdr.axon.permissions.WellKnownPermissions;
import com.klnsdr.axon.permissions.entity.Permission;
import com.klnsdr.axon.permissions.service.PermissionService;
import com.klnsdr.axon.user.entity.UserEntity;
import com.klnsdr.axon.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

class BackendApplicationTest {
    private PermissionService permissionService;
    private UserService userService;
    private ExitHandler exitHandler;
    private BackendApplication backendApplication;

    @BeforeEach
    void setUp() {
        permissionService = mock(PermissionService.class);
        userService = mock(UserService.class);
        exitHandler = mock(ExitHandler.class);
        backendApplication = new BackendApplication(permissionService, userService, exitHandler);
    }

    @Test
    void initialize_callsExitWhenDevPermissionDoesNotExist() {
        when(permissionService.existsDevPermission()).thenReturn(false);

        backendApplication.initialize();

        verify(exitHandler).exit(1);
        verify(permissionService).existsDevPermission();
        verifyNoMoreInteractions(permissionService, userService);
    }

    @Test
    void initialize_callsExitWhenPermissionByNameIsEmpty() {
        when(permissionService.existsDevPermission()).thenReturn(true);
        when(permissionService.getPermissionByName(WellKnownPermissions.DEVELOPER.getName()))
                .thenReturn(Optional.empty());

        backendApplication.initialize();

        verify(exitHandler).exit(1);
        verify(permissionService).existsDevPermission();
        verify(permissionService).getPermissionByName(WellKnownPermissions.DEVELOPER.getName());
        verifyNoMoreInteractions(userService);
    }

    @Test
    void initialize_logsWarningWhenNoAdminUserFound() {
        when(permissionService.existsDevPermission()).thenReturn(true);
        when(permissionService.getPermissionByName(WellKnownPermissions.DEVELOPER.getName()))
                .thenReturn(Optional.of(new Permission()));
        when(userService.getAdminUser()).thenReturn(Optional.empty());

        backendApplication.initialize();

        verify(userService).getAdminUser();
        verifyNoInteractions(exitHandler);
    }

    @Test
    void initialize_grantsPermissionToAdminUserAndLogsInfo() {
        Permission developerPermission = new Permission();
        UserEntity adminUser = new UserEntity();
        adminUser.setName("admin");

        when(permissionService.existsDevPermission()).thenReturn(true);
        when(permissionService.getPermissionByName(WellKnownPermissions.DEVELOPER.getName()))
                .thenReturn(Optional.of(developerPermission));
        when(userService.getAdminUser()).thenReturn(Optional.of(adminUser));
        when(permissionService.addPermissionToUser(adminUser, developerPermission)).thenReturn(true);

        backendApplication.initialize();

        verify(permissionService).addPermissionToUser(adminUser, developerPermission);
        verifyNoInteractions(exitHandler);
    }

    @Test
    void initialize_doesNotCallExitIfPermissionNotAdded() {
        Permission developerPermission = new Permission();
        UserEntity adminUser = new UserEntity();
        adminUser.setName("admin");

        when(permissionService.existsDevPermission()).thenReturn(true);
        when(permissionService.getPermissionByName(WellKnownPermissions.DEVELOPER.getName()))
                .thenReturn(Optional.of(developerPermission));
        when(userService.getAdminUser()).thenReturn(Optional.of(adminUser));
        when(permissionService.addPermissionToUser(adminUser, developerPermission)).thenReturn(false);

        backendApplication.initialize();

        verify(permissionService).addPermissionToUser(adminUser, developerPermission);
        verifyNoInteractions(exitHandler);
    }
}