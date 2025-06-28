package com.klnsdr.axon.blog;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/blog")
public class BlogResource {
    @PostMapping("/{ID}")
    public ResponseEntity<?> createBlogPost() {
        return ResponseEntity.status(501).build();
    }

    @GetMapping("/{ID}")
    public ResponseEntity<?> getBlogPosts() {
        return ResponseEntity.status(501).build();
    }

    @GetMapping("/{blogId}/article/articleID")
    public ResponseEntity<?> getBlogPost() {
        return ResponseEntity.status(501).build();
    }
}
