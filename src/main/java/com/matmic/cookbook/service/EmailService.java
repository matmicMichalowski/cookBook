package com.matmic.cookbook.service;

import com.matmic.cookbook.domain.User;

public interface EmailService {
    void sendEmail(String to, String subject, String content);
    void sendEmailFromTemplate(User user, String templateName, String subject);
    void activationEmail(User user);
    void passwordResetEmail(User user);
}
