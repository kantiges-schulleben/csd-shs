package com.klnsdr.axon.shs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shs")
public class ShsResource {
    @PostMapping("/students/enroll")
    public ResponseEntity<?> enrollStudent() {
        return ResponseEntity.status(501).build();
    }
}
