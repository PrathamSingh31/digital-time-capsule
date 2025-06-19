package com.capsule.controller;

import com.capsule.dto.MessageRequest;
import com.capsule.model.UserMessage;
import com.capsule.security.UserPrincipal;
import com.capsule.service.UserMessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
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

    // ✅ Create a new message
    @PostMapping
    public ResponseEntity<?> createMessage(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                           @Valid @RequestBody MessageRequest request) {
        if (userPrincipal == null) {
            return ResponseEntity.status(401).body("Unauthorized access.");
        }

        try {
            Long userId = userPrincipal.getId();
            System.out.println("Authenticated user ID: " + userId); // 🔍 Debug user ID

            UserMessage message = userMessageService.createMessage(userId, request);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            e.printStackTrace(); // 👈 full stack trace
            return ResponseEntity.badRequest().body("Failed to create message: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<UserMessage>> getMessages(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getId();
        List<UserMessage> messages = userMessageService.getMessagesByUserId(userId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<UserMessage>> filterMessages(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(required = false) Integer year,
            @RequestParam(defaultValue = "asc") String sort) {
        Long userId = userPrincipal.getId();
        List<UserMessage> filtered = userMessageService.getFilteredMessages(userId, year, sort);
        return ResponseEntity.ok(filtered);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMessage(@PathVariable Long id,
                                           @AuthenticationPrincipal UserPrincipal userPrincipal,
                                           @Valid @RequestBody MessageRequest request) {
        try {
            UserMessage updated = userMessageService.updateMessage(id, userPrincipal.getId(), request);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating message: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long id,
                                           @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            userMessageService.deleteMessage(id, userPrincipal.getId());
            return ResponseEntity.ok("Message deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting message: " + e.getMessage());
        }
    }

    @PostMapping("/import")
    public ResponseEntity<?> importMessages(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                            @RequestBody List<MessageRequest> messages) {
        try {
            System.out.println("Received messages: " + messages);
            userMessageService.importMessages(userPrincipal.getId(), messages);
            return ResponseEntity.ok("Messages imported successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to import messages: " + e.getMessage());
        }
    }

    @GetMapping("/scheduled")
    public ResponseEntity<List<UserMessage>> getScheduledMessages(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getId();
        List<UserMessage> scheduled = userMessageService.getScheduledMessages(userId);
        return ResponseEntity.ok(scheduled);
    }

    @GetMapping("/export")
    public ResponseEntity<Resource> exportMessages(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            List<UserMessage> messages = userMessageService.getMessagesByUserUsername(userPrincipal.getUsername());
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(messages);

            ByteArrayResource resource = new ByteArrayResource(json.getBytes());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=messages.json");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentLength(json.getBytes().length)
                    .body(resource);
        } catch (Exception e) {
            byte[] errorBytes = ("Error exporting messages: " + e.getMessage()).getBytes();
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(new ByteArrayResource(errorBytes));
        }
    }
}
