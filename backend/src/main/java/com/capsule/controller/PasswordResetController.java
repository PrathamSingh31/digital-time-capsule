package com.capsule.controller;

import com.capsule.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    // ✅ Endpoint to request a password reset token via email
    @PostMapping("/request-reset")
    public ResponseEntity<?> requestPasswordReset(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required.");
        }

        try {
            passwordResetService.initiateReset(email);
            return ResponseEntity.ok("✅ Password reset email sent successfully.");
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("❌ Failed to send reset email: " + e.getMessage());
        }
    }

    // ✅ Endpoint to reset password using token
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");

        if (token == null || token.trim().isEmpty() || newPassword == null || newPassword.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Token and new password are required.");
        }

        try {
            passwordResetService.resetPassword(token, newPassword);
            return ResponseEntity.ok("✅ Password updated successfully.");
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("❌ Failed to reset password: " + e.getMessage());
        }
    }
}
