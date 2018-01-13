package com.matmic.cookbook.service.mail;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Mail {


    private String mailTo;
    private String recipientName;
    private String mailSubject;
    private String mailContent;
    private String actionLink;

    public Mail(String mailTo, String recipientName, String mailSubject, String mailContent, String actionLink){
        this.mailTo = mailTo;
        this.recipientName = recipientName;
        this.mailSubject = mailSubject;
        this.mailContent = mailContent;
        this.actionLink = actionLink;
    }

}
