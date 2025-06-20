package com.capsule.controller;

import com.capsule.service.ScheduledEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email/scheduled")
@CrossOrigin(origins = "http://localhost:5174")  // ✅ Match your frontend port
public class ScheduledEmailController {

    @Autowired
    private ScheduledEmailService scheduledEmailService;

    // Endpoint to manually trigger today's scheduled email delivery
    @PostMapping("/test")
    public ResponseEntity<String> triggerTodayEmails() {
        scheduledEmailService.sendTodayManually();
        return ResponseEntity.ok("✅ Scheduled email delivery triggered for today.");
    }
}
