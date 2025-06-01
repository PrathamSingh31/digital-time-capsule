package com.capsule.controller;

import com.capsule.model.User;
import com.capsule.model.UserMessage;
import com.capsule.dto.MessageRequest;
import com.capsule.security.UserPrincipal;
import com.capsule.service.UserMessageService;
import com.capsule.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserMessageController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMessageService userMessageService;

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        User user = userService.getUserById(principal.getId());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    public ResponseEntity<UserMessage> createMessage(@RequestBody MessageRequest messageRequest, Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        UserMessage savedMessage = userMessageService.createMessage(principal.getId(), messageRequest);
        return ResponseEntity.ok(savedMessage);
    }

    @GetMapping("/messages")
    public ResponseEntity<List<UserMessage>> getMessages(
            Authentication authentication,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "sort", required = false) String sort
    ) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        List<UserMessage> messages = userMessageService.getMessagesByUser(principal.getId(), year, sort);
        return ResponseEntity.ok(messages);
    }
}
