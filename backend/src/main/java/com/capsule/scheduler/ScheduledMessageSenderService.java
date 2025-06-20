package com.capsule.scheduler;

import com.capsule.model.UserMessage;
import com.capsule.repository.UserMessageRepository;
import com.capsule.service.EmailService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ScheduledMessageSenderService {

    @Autowired
    private UserMessageRepository userMessageRepository;

    @Autowired
    private EmailService emailService;

    /**
     * This method runs every day at 9:00 AM server time.
     * It sends reminder emails for messages scheduled to be delivered today.
     */
    @Scheduled(cron = "0 0 9 * * *") // ⏰ Every day at 9 AM
    @Transactional
    public void sendScheduledMessages() {
        LocalDate today = LocalDate.now();

        List<UserMessage> messagesToSend = userMessageRepository.findAll().stream()
                .filter(message -> message.getMessageDateTime().toLocalDate().equals(today))
                .filter(message -> message.getUser() != null && message.getUser().isEmailRemindersEnabled())
                .toList();

        for (UserMessage message : messagesToSend) {
            String to = message.getUser().getEmail();
            String subject = "📨 Your Digital Time Capsule Message for Today";
            String body = "Hi " + message.getUser().getUsername() + ",\n\n"
                    + "Here's a message you saved for yourself:\n\n"
                    + "🔖 Title: " + message.getTitle() + "\n\n"
                    + message.getContent() + "\n\n"
                    + "— From your Digital Time Capsule 💌";

            emailService.sendEmail(to, subject, body);
        }
    }
}
