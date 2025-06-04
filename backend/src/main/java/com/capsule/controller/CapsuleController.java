package com.capsule.controller;

import com.capsule.model.Capsule;
import com.capsule.service.CapsuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/capsule")
@CrossOrigin(origins = "http://localhost:5174")

public class CapsuleController {

    private final CapsuleService capsuleService;

    public CapsuleController(CapsuleService capsuleService) {
        this.capsuleService = capsuleService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCapsule(@RequestBody Map<String, String> payload,
                                           Principal principal) {
        String message = payload.get("message");
        String unlockDate = payload.get("unlockDate"); // format: YYYY-MM-DD
        String username = principal.getName(); // from JWT

        Capsule capsule = capsuleService.createCapsule(message, username, unlockDate);
        return ResponseEntity.ok(capsule);
    }

    @GetMapping("/my-capsules")
    public ResponseEntity<List<Capsule>> getMyCapsules(Principal principal) {
        String username = principal.getName();
        List<Capsule> capsules = capsuleService.getAccessibleCapsules(username);
        return ResponseEntity.ok(capsules);
    }
}
