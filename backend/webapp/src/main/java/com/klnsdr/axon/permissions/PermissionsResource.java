package com.klnsdr.axon.permissions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/permissions")
public class PermissionsResource {
    @GetMapping("/")
    public ResponseEntity<?> getPermissions() {
        return ResponseEntity.status(501).build();
    }

    @PostMapping("/")
    public ResponseEntity<?> createPermission() {
        return ResponseEntity.status(501).build();
    }
}
