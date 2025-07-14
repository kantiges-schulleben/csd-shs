package com.klnsdr.axon.menu.service;

import com.klnsdr.axon.menu.entity.MenuItemEntity;
import com.klnsdr.axon.permissions.WellKnownPermissions;
import com.klnsdr.axon.permissions.entity.UserPermissions;
import com.klnsdr.axon.permissions.service.PermissionService;
import com.klnsdr.axon.user.service.UserService;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class MenuService {
    private static final String LOGIN_MENU_NAME = "Login";
    private static final String LOGIN_MENU_LOCATION = "/oauth2/authorization/github";
    private static final String LOGOUT_MENU_NAME = "Logout";
    private static final String LOGOUT_MENU_LOCATION = "/api/users/logout";
    private static final String DEVELOPER_MENU_NAME = "Developer";
    private static final String DEVELOPER_MENU_LOCATION = "/controlpanel";
    private static final String SHS_ADMIN_MENU_NAME = "SHS Admin";
    private static final String SHS_ADMIN_MENU_LOCATION = "/shs/admin";

    private static final MenuItemEntity LOGIN_MENU_ITEM = new MenuItemEntity(LOGIN_MENU_NAME, LOGIN_MENU_LOCATION);
    private static final MenuItemEntity LOGOUT_MENU_ITEM = new MenuItemEntity(LOGOUT_MENU_NAME, LOGOUT_MENU_LOCATION);
    private static final MenuItemEntity DEVELOPER_MENU_ITEM = new MenuItemEntity(DEVELOPER_MENU_NAME, DEVELOPER_MENU_LOCATION);
    private static final MenuItemEntity SHS_ADMIN_MENU_ITEM = new MenuItemEntity(SHS_ADMIN_MENU_NAME, SHS_ADMIN_MENU_LOCATION);

    private static final List<MenuItemEntity> MENU_PUBLIC_USER = List.of(LOGIN_MENU_ITEM);

    private static final Map<String, MenuItemEntity> MENU_ITEM_MAP = Map.of(
            WellKnownPermissions.DEVELOPER.getName(), DEVELOPER_MENU_ITEM,
            WellKnownPermissions.SHS_ADMIN.getName(), SHS_ADMIN_MENU_ITEM
    );

    private final PermissionService permissionService;

    public MenuService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    public List<MenuItemEntity> getUserMenu(Principal principal) {
        if (principal == null) {
            return MENU_PUBLIC_USER;
        }

        final List<UserPermissions> permissions =
                permissionService
                    .getUserPermissions(Long.parseLong(principal.getName())) // Assuming principal.getName() returns the user ID as a String
                    .stream()
                    .sorted(
                            Comparator.comparing(p -> p.getPermission().getId())
                    ).toList();

        final List<MenuItemEntity> menuItems = new ArrayList<>();

        if (hasDeveloperPermission(permissions)) {
            menuItems.addAll(MENU_ITEM_MAP.values());
        } else {
            for (UserPermissions permission : permissions) {
                final MenuItemEntity menuItem = MENU_ITEM_MAP.get(permission.getPermission().getInternalName());
                menuItems.add(menuItem);
            }
        }

        menuItems.sort(Comparator.comparing(MenuItemEntity::getName));

        menuItems.add(LOGOUT_MENU_ITEM);
        return menuItems;
    }

    private boolean hasDeveloperPermission(List<UserPermissions> permissions) {
        return permissions.stream()
                .anyMatch(p -> p.getPermission().getInternalName().equals(WellKnownPermissions.DEVELOPER.getName()));
    }
}
