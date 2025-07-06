package com.klnsdr.axon.menu.service;

import com.klnsdr.axon.menu.entity.MenuItemEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class MenuService {
    private static final String LOGIN_MENU_NAME = "Login";
    private static final String LOGIN_MENU_LOCATION = "/oauth2/authorization/github";
    private static final String LOGOUT_MENU_NAME = "Logout";
    private static final String LOGOUT_MENU_LOCATION = "/api/users/logout";

    private static final MenuItemEntity LOGIN_MENU_ITEM = new MenuItemEntity(LOGIN_MENU_NAME, LOGIN_MENU_LOCATION);
    private static final MenuItemEntity LOGOUT_MENU_ITEM = new MenuItemEntity(LOGOUT_MENU_NAME, LOGOUT_MENU_LOCATION);

    private static final List<MenuItemEntity> MENU_PUBLIC_USER = List.of(LOGIN_MENU_ITEM);

    public List<MenuItemEntity> getUserMenu(Principal principal) {
        if (principal == null) {
            return MENU_PUBLIC_USER;
        }
        return List.of(
            LOGOUT_MENU_ITEM
        );
    }
}
