package com.klnsdr.axon.images;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/images")
public class ImagesResource {
    @GetMapping("/{ID}")
    public ResponseEntity<?> getImage() {
        return ResponseEntity.status(501).build();
    }
}
