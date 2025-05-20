package com.capsule.controller;

import com.capsule.dto.MessageRequest;
import com.capsule.service.UserMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserMessageController {

    private final UserMessageService userMessageService;

    public UserMessageController(UserMessageService userMessageService) {
        this.userMessageService = userMessageService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createMessage(@RequestBody MessageRequest messageRequest, Principal principal) {
        String username = principal.getName();
        LocalDate unlockDate = messageRequest.getUnlockDate(); // ✅ Already a LocalDate
        userMessageService.saveMessage(username, messageRequest.getMessage(), unlockDate);
        return ResponseEntity.ok(Map.of("message", "Message saved successfully!"));
    }

    @GetMapping("/messages")
    public ResponseEntity<?> getMessages(Principal principal) {
        String username = principal.getName();
        return ResponseEntity.ok(userMessageService.getMessagesByUsername(username));
    }
}
