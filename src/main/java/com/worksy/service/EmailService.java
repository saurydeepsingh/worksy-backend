package com.worksy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Sends transactional emails (registration, application updates, interview scheduling, etc).
 * Controlled by app.mail.enabled — when false (default for local dev without SMTP creds
 * configured), emails are logged instead of sent, so the rest of the app still works
 * end-to-end without requiring real SMTP credentials.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.enabled:false}")
    private boolean mailEnabled;

    @Value("${app.mail.from}")
    private String fromAddress;

    public void sendEmail(String to, String subject, String body) {
        if (!mailEnabled) {
            log.info("[MAIL DISABLED] To: {} | Subject: {} | Body: {}", to, subject, body);
            return;
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }

    public void sendRegistrationEmail(String to, String fullName) {
        sendEmail(to, "Welcome to WORKSY",
                "Hi " + fullName + ",\n\nWelcome to WORKSY - Find Jobs. Build Skills. Get Hired.\n\nYour account has been created successfully.");
    }

    public void sendApplicationConfirmationEmail(String to, String jobTitle) {
        sendEmail(to, "Application Received - " + jobTitle,
                "Your application for \"" + jobTitle + "\" has been received and is under review.");
    }

    public void sendStatusUpdateEmail(String to, String jobTitle, String status) {
        sendEmail(to, "Application Update - " + jobTitle,
                "Your application status for \"" + jobTitle + "\" has been updated to: " + status);
    }

    public void sendInterviewScheduledEmail(String to, String jobTitle, String dateTime) {
        sendEmail(to, "Interview Scheduled - " + jobTitle,
                "Your interview for \"" + jobTitle + "\" has been scheduled on " + dateTime + ".");
    }

    public void sendPasswordResetEmail(String to, String resetLink) {
        sendEmail(to, "Reset Your WORKSY Password",
                "Click the link to reset your password: " + resetLink + "\nThis link expires in 30 minutes.");
    }
}
