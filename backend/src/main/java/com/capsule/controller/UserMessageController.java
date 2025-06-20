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
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user/messages")
@CrossOrigin(origins = "http://localhost:5173")
public class UserMessageController {

    @Autowired
    private UserMessageService userMessageService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<?> createMessage(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                           @Valid @RequestBody MessageRequest request) {
        if (userPrincipal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access.");
        }

        try {
            Long userId = userPrincipal.getId();
            UserMessage message = userMessageService.createMessage(userId, request);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
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
    public ResponseEntity<List<UserMessage>> filterMessages(@AuthenticationPrincipal UserPrincipal userPrincipal,
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
            userMessageService.importMessages(userPrincipal.getId(), messages);
            return ResponseEntity.ok("Messages imported successfully.");
        } catch (Exception e) {
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

    // ✅ GET SHARED MESSAGE BY TOKEN (Public View)
    @GetMapping("/share/{token}")
    public ResponseEntity<?> getSharedMessage(@PathVariable String token) {
        UserMessage sharedMessage = userMessageService.getMessageByShareToken(token);
        if (sharedMessage == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid or expired share link.");
        }

        Map<String, Object> publicMessage = new HashMap<>();
        publicMessage.put("title", sharedMessage.getTitle());
        publicMessage.put("content", sharedMessage.getContent());
        publicMessage.put("date", sharedMessage.getMessageDateTime());

        // ✅ Add imageUrl if it exists
        if (sharedMessage.getImageUrl() != null && !sharedMessage.getImageUrl().isEmpty()) {
            publicMessage.put("imageUrl", sharedMessage.getImageUrl());
        }

        return ResponseEntity.ok(publicMessage);
    }




    // ✅ GENERATE OR RETURN EXISTING TOKEN
    @PutMapping("/{id}/share")
    public ResponseEntity<String> shareMessage(@PathVariable Long id,
                                               @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            String token = userMessageService.enableSharing(id, userPrincipal.getId());
            String shareableUrl = "http://localhost:5173/share/" + token;
            return ResponseEntity.ok(shareableUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to generate share link: " + e.getMessage());
        }
    }

    // ✅ DISABLE TOKEN
    @PutMapping("/{id}/unshare")
    public ResponseEntity<String> unshareMessage(@PathVariable Long id,
                                                 @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            userMessageService.disableSharing(id, userPrincipal.getId());
            return ResponseEntity.ok("Sharing disabled for message ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to disable sharing: " + e.getMessage());
        }
    }

    @PostMapping("/upload-image")
    public ResponseEntity<?> uploadMessageWithImage(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("deliveryDate") String deliveryDate,
            @RequestParam("image") MultipartFile imageFile) {

        try {
            UserMessage message = userMessageService.createMessageWithImage(
                    userPrincipal.getId(), title, content, deliveryDate, imageFile
            );
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to upload image: " + e.getMessage());
        }
    }

}
