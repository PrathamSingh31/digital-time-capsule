package com.capsule.controller;

import com.capsule.model.User;
import com.capsule.security.UserPrincipal;
import com.capsule.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5174") // Update to match frontend port if needed
public class UserProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(@org.springframework.security.core.annotation.AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getId();
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }
}
