package com.capsule.service;

import com.capsule.model.User;
import com.capsule.model.UserMessage;
import com.capsule.repository.UserMessageRepository;
import com.capsule.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ScheduledEmailService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMessageRepository userMessageRepository;

    @Autowired
    private EmailService emailService;

    // ✅ Automatically runs every day at 9:00 AM server time
    @Scheduled(cron = "0 0 9 * * *")
    public void sendScheduledMessages() {
        LocalDate today = LocalDate.now();

        List<User> users = userRepository.findAll();

        for (User user : users) {
            if (user.isEmailRemindersEnabled()) {
                List<UserMessage> messages = userMessageRepository.findByUserAndMessageDateTimeBetween(
                        user,
                        today.atStartOfDay(),
                        today.plusDays(1).atStartOfDay()
                );

                for (UserMessage message : messages) {
                    emailService.sendEmail(
                            user.getEmail(),
                            "📨 Your Scheduled Time Capsule Message: " + message.getTitle(),
                            message.getContent()
                    );
                }
            }
        }
    }

    // ✅ Triggered manually via endpoint for testing
    public void sendTodayManually() {
        sendScheduledMessages();
    }
}
