package com.klnsdr.axon.users;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users") public class UsersResource {
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

    @GetMapping("/{ID}/menu")
    public ResponseEntity<?> getUserMenu() {
        return ResponseEntity.status(501).build();
    }
}
