package com.klnsdr.axon.users;

import com.klnsdr.axon.menu.entity.MenuItemEntity;
import com.klnsdr.axon.menu.service.MenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users") public class UsersResource {
    private final MenuService menuService;

    public UsersResource(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUsers() {
        return ResponseEntity.status(501).build();
    }

    @GetMapping("/")
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.status(501).build();
    }

    @GetMapping("/{ID}")
    public ResponseEntity<?> getUser() {
        return ResponseEntity.status(501).build();
    }

    @PutMapping("/{ID}")
    public ResponseEntity<?> updateUser() {
        return ResponseEntity.status(501).build();
    }

    @DeleteMapping("/{ID}")
    public ResponseEntity<?> deleteUser() {
        return ResponseEntity.status(501).build();
    }

    @GetMapping("/menu")
    public List<MenuItemEntity> getUserMenu(Principal principal) {
        return menuService.getUserMenu(principal);
    }
}
