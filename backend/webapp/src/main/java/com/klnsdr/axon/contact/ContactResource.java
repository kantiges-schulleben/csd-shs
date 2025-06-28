package com.klnsdr.axon.contact;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contact")
public class ContactResource {
    @PostMapping("/")
    public ResponseEntity<?> createContact() {
        return ResponseEntity.status(501).build();
    }
}
