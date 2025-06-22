package com.capsule.controller;

import com.capsule.dto.MessageRequest;
import com.capsule.model.UserMessage;
import com.capsule.security.UserPrincipal;
import com.capsule.service.UserMessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@CrossOrigin(origins = "http://localhost:5173") // Replace or move to config if needed
public class UserMessageController {

    private static final Logger logger = LoggerFactory.getLogger(UserMessageController.class);

    @Autowired
    private UserMessageService userMessageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${frontend.base.url:http://localhost:5173}")
    private String frontendBaseUrl;



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
            logger.error("Failed to create message", e);
            return ResponseEntity.badRequest().body("Failed to create message: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getMessages(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access.");
        }

        Long userId = userPrincipal.getId();
        List<UserMessage> messages = userMessageService.getMessagesByUserId(userId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterMessages(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                            @RequestParam(required = false) Integer year,
                                            @RequestParam(defaultValue = "asc") String sort) {
        if (userPrincipal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access.");
        }

        Long userId = userPrincipal.getId();
        List<UserMessage> filtered = userMessageService.getFilteredMessages(userId, year, sort);
        return ResponseEntity.ok(filtered);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateMessageWithMedia(@PathVariable Long id,
                                                    @AuthenticationPrincipal UserPrincipal userPrincipal,
                                                    @RequestParam("title") String title,
                                                    @RequestParam("content") String content,
                                                    @RequestParam("deliveryDate") String deliveryDate,
                                                    @RequestParam(value = "unlockDate", required = false) String unlockDate,
                                                    @RequestParam(value = "image", required = false) MultipartFile imageFile,
                                                    @RequestParam(value = "video", required = false) MultipartFile videoFile) {
        try {
            UserMessage updatedMessage = userMessageService.updateMessageWithMedia(
                    id,
                    userPrincipal.getId(),
                    title,
                    content,
                    deliveryDate,
                    unlockDate, // ✅ Pass unlockDate here
                    imageFile,
                    videoFile
            );
            return ResponseEntity.ok(updatedMessage);
        } catch (Exception e) {
            logger.error("Error updating message", e);
            return ResponseEntity.badRequest().body("Error updating message: " + e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getMessageById(@PathVariable Long id,
                                            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            UserMessage message = userMessageService.getMessageByIdAndUsername(id, userPrincipal.getUsername());
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            logger.error("Error fetching message", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message not found: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long id,
                                           @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            userMessageService.deleteMessage(id, userPrincipal.getId());
            return ResponseEntity.ok("Message deleted successfully.");
        } catch (Exception e) {
            logger.error("Error deleting message", e);
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
            logger.error("Failed to import messages", e);
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
            logger.error("Error exporting messages", e);
            byte[] errorBytes = ("Error exporting messages: " + e.getMessage()).getBytes();
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(new ByteArrayResource(errorBytes));
        }
    }

    @GetMapping("/share/{token}")
    public ResponseEntity<?> getSharedMessage(@PathVariable String token) {
        try {
            UserMessage sharedMessage = userMessageService.getMessageByShareToken(token);
            Map<String, Object> publicMessage = new HashMap<>();
            publicMessage.put("title", sharedMessage.getTitle());
            publicMessage.put("content", sharedMessage.getContent());
            publicMessage.put("date", sharedMessage.getMessageDateTime());

            if (sharedMessage.getImageUrl() != null) {
                publicMessage.put("imageUrl", sharedMessage.getImageUrl());
            }

            if (sharedMessage.getVideoUrl() != null) {
                publicMessage.put("videoUrl", sharedMessage.getVideoUrl());
            }

            return ResponseEntity.ok(publicMessage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid or expired share link.");
        }
    }

    @PutMapping("/{id}/share")
    public ResponseEntity<String> shareMessage(@PathVariable Long id,
                                               @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            String token = userMessageService.enableSharing(id, userPrincipal.getId());
            String shareableUrl = frontendBaseUrl + "/share/" + token;
            return ResponseEntity.ok(shareableUrl);
        } catch (Exception e) {
            logger.error("Failed to generate share link", e);
            return ResponseEntity.badRequest().body("Failed to generate share link: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/unshare")
    public ResponseEntity<String> unshareMessage(@PathVariable Long id,
                                                 @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            userMessageService.disableSharing(id, userPrincipal.getId());
            return ResponseEntity.ok("Sharing disabled for message ID: " + id);
        } catch (Exception e) {
            logger.error("Failed to disable sharing", e);
            return ResponseEntity.badRequest().body("Failed to disable sharing: " + e.getMessage());
        }
    }

    @PostMapping(value = "/upload-media", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadMessageWithMedia(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("deliveryDate") String deliveryDate,
            @RequestParam(value = "unlockDate", required = false) String unlockDate,
            @RequestParam(value = "image", required = false) MultipartFile imageFile,
            @RequestParam(value = "video", required = false) MultipartFile videoFile) {

        try {
            UserMessage message = userMessageService.createMessageWithMedia(
                    userPrincipal.getId(),
                    title,
                    content,
                    deliveryDate,
                    unlockDate,
                    imageFile,
                    videoFile
            );
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            logger.error("Failed to upload media", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to upload media: " + e.getMessage());
        }
    }

}
