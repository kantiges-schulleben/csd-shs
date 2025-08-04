package com.klnsdr.axon.users;

import com.klnsdr.axon.menu.entity.MenuItemEntity;
import com.klnsdr.axon.menu.service.MenuService;
import com.klnsdr.axon.permissions.service.PermissionService;
import com.klnsdr.axon.user.entity.UserEntity;
import com.klnsdr.axon.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users") public class UsersResource {
    private final MenuService menuService;
    private final UserService userService;
    private final PermissionService permissionService;

    public UsersResource(MenuService menuService, UserService userService, PermissionService permissionService) {
        this.menuService = menuService;
        this.userService = userService;
        this.permissionService = permissionService;
    }

    @GetMapping("/search")
    public List<UserEntity> searchUsers(@RequestParam("q") String query) {
        return userService.searchByName(query);
    }

    @GetMapping("/")
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.status(501).build();
    }

    @GetMapping("/{ID}")
    public ResponseEntity<UserEntity> getUser(@PathVariable("ID") Long ID) {
        final Optional<UserEntity> user = userService.findById(ID);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{ID}")
    public ResponseEntity<?> updateUser(@RequestBody UpdatePermissionsDTO updatePermissionsDTO, @PathVariable("ID") Long ID) {
        final boolean success = permissionService.clearAndSetPermissions(ID, updatePermissionsDTO.getPermissionIds());
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/{ID}")
    public ResponseEntity<?> deleteUser(@PathVariable("ID") Long ID) {
        final boolean permissionsDeleted = permissionService.deleteAllUserPermissions(ID);
        if (!permissionsDeleted) {
            return ResponseEntity.status(500).build();
        }

        final UserEntity deletedUser = userService.deleteUser(ID);
        if (deletedUser != null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/menu")
    public List<MenuItemEntity> getUserMenu(Principal principal) {
        return menuService.getUserMenu(principal);
    }
}
