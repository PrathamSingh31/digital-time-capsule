package com.capsule.controller;

import com.capsule.dto.JwtResponse;
import com.capsule.dto.LoginDTO;
import com.capsule.dto.RegisterDTO;
import com.capsule.model.User;
import com.capsule.security.JwtUtil;
import com.capsule.security.UserPrincipal;
import com.capsule.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.capsule.service.EmailService;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5174")

public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterDTO registerDTO) {
        User user = userService.registerNewUser(registerDTO);
        if (user != null) {
            return ResponseEntity.ok("User registered successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User registration failed");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword()
                    )
            );

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userPrincipal);
            return ResponseEntity.ok(new JwtResponse(token, userPrincipal.getUsername()));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PutMapping("/settings/email-reminders")
    public ResponseEntity<String> updateEmailReminders(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam boolean enabled) {
        userService.updateEmailReminderSetting(userPrincipal.getId(), enabled);
        return ResponseEntity.ok("Email reminder setting updated");
    }

    @GetMapping("/settings/email-reminders")
    public ResponseEntity<Boolean> getEmailReminders(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        boolean isEnabled = userService.getEmailReminderSetting(userPrincipal.getId());
        return ResponseEntity.ok(isEnabled);
    }

    @PostMapping("/send-test-reminder")
    public ResponseEntity<String> sendTestReminder(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null || userPrincipal.getEmail() == null) {
            return ResponseEntity.badRequest().body("User not authenticated or email is missing");
        }

        emailService.sendTestReminder(userPrincipal.getEmail());
        return ResponseEntity.ok("Test reminder email sent to: " + userPrincipal.getEmail());
    }



    @PostMapping("/test-reminder")
    public ResponseEntity<String> testReminder(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        emailService.sendTestReminder(user.getEmail());

        return ResponseEntity.ok("Test reminder email sent to: " + user.getEmail());
    }






}
