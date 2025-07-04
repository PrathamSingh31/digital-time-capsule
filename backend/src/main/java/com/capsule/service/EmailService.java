package com.capsule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendTestReminder(String toEmail) {
        String subject = "🧪 Test Reminder from Digital Time Capsule";
        String body = "Hi there!\n\nThis is a test reminder email from your Digital Time Capsule.\n\nIf you see this, email notifications are working correctly!";
        sendEmail(toEmail, subject, body);
    }

    public void sendPasswordResetEmail(String toEmail, String resetLink) {
        String subject = "🔒 Password Reset Request";
        String body = "Hello,\n\nWe received a request to reset your password. Click the link below to reset it:\n\n"
                + resetLink
                + "\n\nIf you did not request this, please ignore this email.\n\nRegards,\nDigital Time Capsule Team";
        sendEmail(toEmail, subject, body);
    }
}
