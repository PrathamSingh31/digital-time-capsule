package com.capsule.controller;

import com.capsule.model.User;
import com.capsule.repository.UserRepository;
import com.capsule.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // --------- AUTH ROUTES ---------

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> user) {
        String username = user.get("username");
        String password = user.get("password");

        try {
            String token = userService.loginUser(username, password);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            userService.registerUser(user);
            return ResponseEntity.ok(Map.of("message", "User registered successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    // --------- PROFILE ROUTE ---------

    @GetMapping("/user/profile")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        return ResponseEntity.ok().body(
                new ProfileResponse(user.getUsername(), user.getEmail())
        );
    }

    // DTO for profile response
    static class ProfileResponse {
        private final String username;
        private final String email;

        public ProfileResponse(String username, String email) {
            this.username = username;
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }
    }
}
