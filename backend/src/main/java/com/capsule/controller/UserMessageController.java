package com.capsule.controller;

import com.capsule.model.UserMessage;
import com.capsule.dto.MessageRequest;
import com.capsule.security.UserPrincipal;
import com.capsule.service.UserMessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/messages")
@CrossOrigin(origins = "http://localhost:5173")
public class UserMessageController {

    @Autowired
    private UserMessageService userMessageService;

    @Autowired
    private ObjectMapper objectMapper;

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

    @PutMapping("/{id}")
    public ResponseEntity<UserMessage> updateMessage(@PathVariable Long id,
                                                     @AuthenticationPrincipal UserPrincipal userPrincipal,
                                                     @RequestBody MessageRequest request) {
        UserMessage updated = userMessageService.updateMessage(id, userPrincipal.getId(), request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long id,
                                           @AuthenticationPrincipal UserPrincipal userPrincipal) {
        userMessageService.deleteMessage(id, userPrincipal.getId());
        return ResponseEntity.ok("Message deleted successfully");
    }

    @PostMapping("/import")
    public ResponseEntity<?> importMessages(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                            @RequestBody List<MessageRequest> messages) {
        try {
            Long userId = userPrincipal.getId();
            userMessageService.importMessages(userId, messages);
            return ResponseEntity.ok("Messages imported successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to import messages: " + e.getMessage());
        }
    }

    @GetMapping("/export")
    public ResponseEntity<Resource> exportMessages(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            List<UserMessage> messages = userMessageService.getMessagesByUserUsername(userPrincipal.getUsername());
            String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(messages);

            byte[] bytes = jsonString.getBytes();
            ByteArrayResource resource = new ByteArrayResource(bytes);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=exported-messages.json");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(bytes.length)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (Exception e) {
            byte[] errorBytes = ("Error exporting messages: " + e.getMessage()).getBytes();
            ByteArrayResource errorResource = new ByteArrayResource(errorBytes);
            return ResponseEntity.internalServerError().body(errorResource);
        }
    }
}
