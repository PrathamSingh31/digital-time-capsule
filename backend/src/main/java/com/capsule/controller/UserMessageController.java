package com.capsule.controller;

import com.capsule.model.UserMessage;
import com.capsule.service.UserMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserMessageController {

    private final UserMessageService userMessageService;

    public UserMessageController(UserMessageService userMessageService) {
        this.userMessageService = userMessageService;
    }

    @GetMapping("/messages")
    public ResponseEntity<List<UserMessage>> getUserMessages(@AuthenticationPrincipal UserDetails userDetails) {
        List<UserMessage> messages = userMessageService.getMessagesByUsername(userDetails.getUsername());
        return ResponseEntity.ok(messages);
    }

}
