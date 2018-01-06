package com.matmic.cookbook.service;

import com.matmic.cookbook.domain.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Locale;


@Service
public class EmailServiceImpl implements EmailService {

    private final String EMAIL_FROM = "cookbook.appservice@gmail.com";
    private final String USER = "User";
    private final JavaMailSender javaMailSender;
    private final MessageSource messageSource;
    private final SpringTemplateEngine templateEngine;

    public EmailServiceImpl(JavaMailSender javaMailSender, MessageSource messageSource, @Qualifier("engineForMail") SpringTemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
    }

    @Async
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

    @Async
    @Override
    public void sendEmailFromTemplate(User user, String templateName, String subject) {
        Locale locale = Locale.getDefault();
        Context ctx = new Context(locale);
        ctx.setVariable(USER, user);
        String content = templateEngine.process(templateName, ctx);
        String emailSubject = messageSource.getMessage(subject, null, locale);
        sendEmail(user.getEmail(), emailSubject, content);
    }

    @Async
    @Override
    public void activationEmail(User user) {
        sendEmailFromTemplate(user, "activationEmailTemplate", "emails.activation.title");
    }

    @Async
    @Override
    public void passwordResetEmail(User user) {
        sendEmailFromTemplate(user, "resetPasswordEmailTemplate", "emails.reset.title");

    }
}
