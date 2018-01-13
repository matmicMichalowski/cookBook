package com.matmic.cookbook.service;

import com.matmic.cookbook.service.mail.Mail;

public interface EmailService {
    void sendEmailMessage(Mail mail);
}
