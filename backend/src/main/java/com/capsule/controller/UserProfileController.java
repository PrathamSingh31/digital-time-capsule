package com.capsule.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserProfileController {

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        String username = (String) authentication.getPrincipal(); // JWT sets this

        // You can add logic to fetch full user details if needed
        return ResponseEntity.ok(Map.of("username", username));
    }
}
