package com.capsule.controller;

import com.capsule.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@CrossOrigin(origins = "http://localhost:5173")
public class EmailTestController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/test")
    public String sendTestEmail(@RequestParam String to,
                                @RequestParam String subject,
                                @RequestParam String body) {
        try {
            emailService.sendEmail(to, subject, body);
            return "Email sent successfully to " + to;
        } catch (Exception e) {
            return "Failed to send email: " + e.getMessage();
        }
    }
}
