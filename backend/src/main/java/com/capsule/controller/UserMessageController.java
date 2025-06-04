package com.capsule.controller;

import com.capsule.model.UserMessage;
import com.capsule.dto.MessageRequest;
import com.capsule.security.UserPrincipal;
import com.capsule.service.UserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/messages")
@CrossOrigin(origins = "http://localhost:5174")
public class UserMessageController {

    @Autowired
    private UserMessageService userMessageService;

    @PostMapping
    public ResponseEntity<UserMessage> createMessage(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                     @RequestBody MessageRequest request) {
        Long userId = userPrincipal.getId();
        UserMessage message = userMessageService.createMessage(userId, request);
        return ResponseEntity.ok(message);
    }

    @GetMapping
    public ResponseEntity<List<UserMessage>> getMessages(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getId();
        List<UserMessage> messages = userMessageService.getMessagesByUserId(userId);
        return ResponseEntity.ok(messages);
    }
}
