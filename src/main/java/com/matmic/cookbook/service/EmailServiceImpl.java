package com.matmic.cookbook.service;


import com.matmic.cookbook.service.mail.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

/**
 * Service Implementation for managing Email
 */
@Service
public class EmailServiceImpl implements EmailService {

    private Logger log = LoggerFactory.getLogger(EmailService.class);

    private static final String FROM = "cookbook.appservice@gmail.com";


    private final JavaMailSender mailSender;

    private final SpringTemplateEngine templateEngine;

    public EmailServiceImpl(JavaMailSender mailSender,@Qualifier("templateEngine") SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    /**
     * Send email
     *
     * @param mail model to get data from, to send email to user
     */
    @Async
    @Override
    public void sendEmailMessage(Mail mail){

        MimeMessage message = mailSender.createMimeMessage();

        try{
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            Context ctx = new Context();
            ctx.setVariable("from", FROM);
            ctx.setVariable("to", mail.getMailTo());
            ctx.setVariable("userName", mail.getRecipientName());
            ctx.setVariable("subject", mail.getMailSubject());
            ctx.setVariable("actionLink", mail.getActionLink());
            String html = templateEngine.process(mail.getMailContent(), ctx);

            helper.setTo(mail.getMailTo());
            helper.setText(html, true);
            helper.setSubject(mail.getMailSubject());
            helper.setFrom(FROM);

            mailSender.send(message);

        }catch(MessagingException ex){
            log.warn("Unable to send email {}", ex.getMessage());
        }
    }

}
