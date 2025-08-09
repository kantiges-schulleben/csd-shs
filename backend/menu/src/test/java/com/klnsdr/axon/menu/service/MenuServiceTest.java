package com.klnsdr.axon.menu.service;

import com.klnsdr.axon.menu.entity.MenuItemEntity;
import com.klnsdr.axon.permissions.WellKnownPermissions;
import com.klnsdr.axon.permissions.entity.Permission;
import com.klnsdr.axon.permissions.entity.UserPermissions;
import com.klnsdr.axon.permissions.service.PermissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MenuServiceTest {
    private MenuService menuService;
    private PermissionService permissionServiceMock;
    private Principal principalMock;

    private static final List<MenuItemEntity> publicMenu = List.of(
            new MenuItemEntity("Login", "/oauth2/authorization/nextcloud")
    );
    private static final List<MenuItemEntity> publicLoggedInMenu = List.of(
            new MenuItemEntity("Logout", "/api/users/logout")
    );
    private static final List<MenuItemEntity> shsAdminMenu = List.of(
            new MenuItemEntity("SHS Admin", "/shs/admin"),
            new MenuItemEntity("Logout", "/api/users/logout")
    );
    private static final List<MenuItemEntity> devMenu = List.of(
            new MenuItemEntity("Developer", "/dev"),
            new MenuItemEntity("SHS Admin", "/shs/admin"),
            new MenuItemEntity("Logout", "/api/users/logout")
    );

    @BeforeEach
    public void setUp() {
        permissionServiceMock = mock(PermissionService.class);
        principalMock = mock(Principal.class);
        menuService = new MenuService(permissionServiceMock);
    }

    @Test
    public void getPublicMenuWhenPrincipalIsNull() {
        final List<MenuItemEntity> menu = menuService.getUserMenu(null);
        assertTrue(equals(menu, publicMenu));
    }

    @Test
    public void getPublicMenuWhenPrincipalHasNoPermissions() {
        when(principalMock.getName()).thenReturn("1");
        when(permissionServiceMock.getUserPermissions(1L)).thenReturn(List.of());

        final List<MenuItemEntity> menu = menuService.getUserMenu(principalMock);
        assertTrue(equals(menu, publicLoggedInMenu));
    }

    @Test
    public void getShsAdminMenuWhenPrincipalHasShsAdminPermission() {
        final UserPermissions shsAdminPermission = new UserPermissions();
        final Permission shsAdmin = new Permission();
        shsAdmin.setId(1L);
        shsAdmin.setInternalName(WellKnownPermissions.SHS_ADMIN.getName());
        shsAdminPermission.setPermission(shsAdmin);

        when(principalMock.getName()).thenReturn("1");
        when(permissionServiceMock.getUserPermissions(1L)).thenReturn(List.of(
            shsAdminPermission
        ));

        final List<MenuItemEntity> menu = menuService.getUserMenu(principalMock);
        assertTrue(equals(menu, shsAdminMenu));
    }

    @Test
    public void getFullMenuWhenPrincipalHasDeveloperPermission() {
        final UserPermissions developerPermission = new UserPermissions();
        final Permission developer = new Permission();
        developer.setId(2L);
        developer.setInternalName(WellKnownPermissions.DEVELOPER.getName());
        developerPermission.setPermission(developer);

        when(principalMock.getName()).thenReturn("1");
        when(permissionServiceMock.getUserPermissions(1L)).thenReturn(List.of(
            developerPermission
        ));

        final List<MenuItemEntity> menu = menuService.getUserMenu(principalMock);
        assertTrue(equals(menu, devMenu));
    }

    private boolean equals(List<MenuItemEntity> menu, List<MenuItemEntity> expected) {
        if (menu.size() != expected.size()) {
            return false;
        }
        for (int i = 0; i < menu.size(); i++) {
            if (!equals(menu.get(i), expected.get(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean equals(MenuItemEntity item, MenuItemEntity expected) {
        return item.getName().equals(expected.getName()) &&
               item.getLocation().equals(expected.getLocation());
    }
}
