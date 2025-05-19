package com.capsule.controller;

import com.capsule.dto.MessageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserMessageController {

    @PostMapping("/create")
    public ResponseEntity<String> createMessage(@RequestBody MessageRequest request) {
        // You can add database save logic here
        return ResponseEntity.ok("Message saved: " + request.getMessage() + ", unlock at: " + request.getUnlockDate());
    }
}
