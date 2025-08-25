package com.klnsdr.axon.mail;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Mail {
    private String from;
    private String to;
    private String subject;
    private String body;

    public Mail copy() {
        Mail mail = new Mail();
        mail.setFrom(this.from);
        mail.setTo(this.to);
        mail.setSubject(this.subject);
        mail.setBody(this.body);
        return mail;
    }
}
