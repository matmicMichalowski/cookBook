package com.matmic.cookbook.service;


import com.matmic.cookbook.CookbookApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { CookbookApplication.class })
public class EmailServiceImplTest {

    @Spy
    private JavaMailSender javaMailSender;

    @Autowired
    @Qualifier("templateEngine")
    private SpringTemplateEngine templateEngine;

    @Captor
    private ArgumentCaptor argumentCaptor;


    private EmailService emailService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        doNothing().when(javaMailSender).send(any(MimeMessage.class));
        emailService = new EmailServiceImpl(javaMailSender, templateEngine);
    }

    @Test
    public void sendEmail() throws Exception {
//        String randomToken = UUID.randomUUID().toString();
//        Mail mail = new Mail("test@mail.com", "ExampleName", "Test Subject",
//                "testContent.html", randomToken);
//        Session session = Session.getDefaultInstance(new Properties());
//
//        Context ctx = new Context();
//        MimeMessage mime = new MimeMessage(session);
//        when(javaMailSender.createMimeMessage()).thenReturn(mime);
//        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("testContent");
//        emailService.sendEmailMessage(mail);
//        verify(javaMailSender).send((MimeMessage) argumentCaptor.capture());
//        MimeMessage msg = (MimeMessage) argumentCaptor.getValue();
//
//        assertThat(msg.getSubject()).isEqualTo("Test Subject");
//        assertThat(msg.getAllRecipients()[0]).isEqualTo("test@mail.com");
//        assertThat(msg.getFrom()[0].toString()).isEqualTo("cookbook.appservice@gmail.com");
//        assertThat(msg.getContent()).isInstanceOf(String.class);
//        assertThat(msg.getContent().toString()).isEqualTo("testContent.html");
//        assertThat(msg.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
    }


}