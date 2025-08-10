package com.klnsdr.axon.users;

import com.klnsdr.axon.menu.entity.MenuItemEntity;
import com.klnsdr.axon.menu.service.MenuService;
import com.klnsdr.axon.permissions.service.PermissionService;
import com.klnsdr.axon.user.entity.UserEntity;
import com.klnsdr.axon.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UsersResourceTest {
    private MenuService menuService;
    private UserService userService;
    private PermissionService permissionService;
    private UsersResource usersResource;

    @BeforeEach
    void setUp() {
        menuService = mock(MenuService.class);
        userService = mock(UserService.class);
        permissionService = mock(PermissionService.class);
        usersResource = new UsersResource(menuService, userService, permissionService);
    }

    @Test
    void testSearchUsers() {
        String query = "john";
        final UserEntity user = new UserEntity();
        user.setName("John Doe");

        List<UserEntity> mockUsers = List.of(user);
        when(userService.searchByName(query)).thenReturn(mockUsers);

        List<UserEntity> result = usersResource.searchUsers(query);

        assertEquals(mockUsers, result);
        verify(userService).searchByName(query);
    }

    @Test
    void testGetUsersNotImplemented() {
        ResponseEntity<?> response = usersResource.getUsers();
        assertEquals(501, response.getStatusCode().value());
    }

    @Test
    void testGetUserFound() {
        Long userId = 1L;
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setName("Test User");
        when(userService.findById(userId)).thenReturn(Optional.of(user));

        ResponseEntity<UserEntity> response = usersResource.getUser(userId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(user, response.getBody());
    }

    @Test
    void testGetUserNotFound() {
        Long userId = 1L;
        when(userService.findById(userId)).thenReturn(Optional.empty());

        ResponseEntity<UserEntity> response = usersResource.getUser(userId);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void testUpdateUserSuccess() {
        Long userId = 2L;
        UpdatePermissionsDTO dto = new UpdatePermissionsDTO();
        dto.setPermissionIds(List.of(1L, 2L));
        when(permissionService.clearAndSetPermissions(userId, dto.getPermissionIds())).thenReturn(true);

        ResponseEntity<?> response = usersResource.updateUser(dto, userId);

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void testUpdateUserFailure() {
        Long userId = 2L;
        UpdatePermissionsDTO dto = new UpdatePermissionsDTO();
        dto.setPermissionIds(List.of(1L, 2L));

        when(permissionService.clearAndSetPermissions(userId, dto.getPermissionIds())).thenReturn(false);

        ResponseEntity<?> response = usersResource.updateUser(dto, userId);

        assertEquals(500, response.getStatusCode().value());
    }

    @Test
    void testDeleteUserSuccess() {
        Long userId = 3L;
        when(permissionService.deleteAllUserPermissions(userId)).thenReturn(true);
        UserEntity deletedUser = new UserEntity();
        deletedUser.setId(userId);
        deletedUser.setName("Deleted User");
        when(userService.deleteUser(userId)).thenReturn(deletedUser);

        ResponseEntity<?> response = usersResource.deleteUser(userId);

        assertEquals(204, response.getStatusCode().value());
    }

    @Test
    void testDeleteUserPermissionsFailure() {
        Long userId = 3L;
        when(permissionService.deleteAllUserPermissions(userId)).thenReturn(false);

        ResponseEntity<?> response = usersResource.deleteUser(userId);

        assertEquals(500, response.getStatusCode().value());
        verify(userService, never()).deleteUser(userId);
    }

    @Test
    void testDeleteUserNotFound() {
        Long userId = 3L;
        when(permissionService.deleteAllUserPermissions(userId)).thenReturn(true);
        when(userService.deleteUser(userId)).thenReturn(null);

        ResponseEntity<?> response = usersResource.deleteUser(userId);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void testGetUserMenu() {
        Principal principal = mock(Principal.class);
        List<MenuItemEntity> menu = List.of(new MenuItemEntity());
        when(menuService.getUserMenu(principal)).thenReturn(menu);

        List<MenuItemEntity> result = usersResource.getUserMenu(principal);

        assertEquals(menu, result);
        verify(menuService).getUserMenu(principal);
    }
}
