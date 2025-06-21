package com.capsule.service;

import com.capsule.model.PasswordResetToken;
import com.capsule.model.User;
import com.capsule.repository.PasswordResetTokenRepository;
import com.capsule.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private PasswordResetTokenRepository resetTokenRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    // ✅ 1. Send password reset email with token
    @Transactional
    public void initiateReset(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required.");
        }

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("No user found with that email.");
        }

        User user = optionalUser.get();

        // Delete any existing token for the user
        resetTokenRepo.deleteByUser(user);
        resetTokenRepo.flush();

        // Generate new token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);

        resetTokenRepo.save(resetToken);

        // Send email
        String resetLink = "http://localhost:5173/reset-password?token=" + token;
        String subject = "🔐 Password Reset - Digital Time Capsule";
        String message = "Hi,\n\nWe received a request to reset your password. " +
                "Click the link below to reset it:\n\n" +
                resetLink +
                "\n\nIf you did not request this, you can safely ignore this email.";

        emailService.sendEmail(email, subject, message);
    }

    // ✅ 2. Reset password using the token
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = resetTokenRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired token."));

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Invalidate the token
        resetTokenRepo.delete(resetToken);
    }

    // ✅ 3. Get email associated with a token (optional)
    public String getEmailByResetToken(String token) {
        PasswordResetToken resetToken = resetTokenRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token not found"));
        return resetToken.getUser().getEmail();
    }
}
