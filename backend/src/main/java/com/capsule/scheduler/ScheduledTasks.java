package com.capsule.scheduler;

import com.capsule.model.UserMessage;
import com.capsule.repository.UserMessageRepository;
import com.capsule.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
public class ScheduledTasks {

    @Autowired
    private UserMessageRepository messageRepository;

    @Autowired
    private EmailService emailService;

    // ⏰ Runs every day at 8 AM
    @Scheduled(cron = "0 0 8 * * ?")
    public void sendScheduledMessageReminders() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        List<UserMessage> messages = messageRepository.findByMessageDateTimeBetween(startOfDay, endOfDay);

        for (UserMessage msg : messages) {
            if (msg.getUser() != null && msg.getUser().getEmail() != null) {
                String to = msg.getUser().getEmail();
                String subject = "📨 Time Capsule Message Delivery Reminder";
                String body = "Hi " + msg.getUser().getUsername() + ",\n\n" +
                        "Here's a scheduled message from your Time Capsule:\n\n" +
                        "Title: " + msg.getTitle() + "\n" +
                        "Content: " + msg.getContent() + "\n\n" +
                        "Scheduled Date: " + msg.getMessageDateTime().toLocalDate() + "\n\n" +
                        "⏳ Keep cherishing your memories!\n\n— Digital Time Capsule";

                emailService.sendEmail(to, subject, body);
            }
        }
    }
}
