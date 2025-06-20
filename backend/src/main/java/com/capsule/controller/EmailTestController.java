package com.capsule.controller;

import com.capsule.service.EmailService;
import com.capsule.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@CrossOrigin(origins = "http://localhost:5174")
public class EmailTestController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/test")
    public String sendTestEmail(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                @RequestParam String to,
                                @RequestParam String subject,
                                @RequestParam String body) {
        if (userPrincipal == null) {
            return "❌ User not authenticated.";
        }

        try {
            emailService.sendEmail(to, subject, body);
            return "✅ Email sent to " + to;
        } catch (Exception e) {
            return "❌ Failed to send email: " + e.getMessage();
        }
    }
}
