package com.matmic.cookbook.service;

import com.matmic.cookbook.domain.User;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Locale;


@Service
public class EmailServiceImpl implements EmailService {

    private final String EMAIL_FROM = "cookbook.appservice@gmail.com";
    private final String USER = "User";
    private final String BASE_URL = "baseUrl";
    private final JavaMailSender javaMailSender;
    private final MessageSource messageSource;

    public EmailServiceImpl(JavaMailSender javaMailSender, MessageSource messageSource) {
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
    }


    @Override
    public void sendEmail(String to, String subject, String content) {

        MimeMessage message = javaMailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setTo(to);
            helper.setFrom(EMAIL_FROM);
            helper.setSubject(subject);
            helper.setText(content, true);
        }catch(MessagingException e){
            e.getMessage();
        }

    }

    @Override
    public void sendEmailFromTemplate(User user, String templateName, String subject) {
        Context ctx = new Context(Locale.getDefault());
        ctx.setVariable(USER, user);
        ctx.setVariable(BASE_URL, user);
    }

    @Override
    public void activationEmail(User user) {

    }

    @Override
    public void passwordResetEmail(User user) {

    }
}
