package com.capsule.service;

public interface EmailService {
    void sendEmail(String to, String subject, String text);
    void sendTestReminder(String toEmail);
}
