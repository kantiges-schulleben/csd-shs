package com.klnsdr.axon.shs.mail;

import com.klnsdr.axon.mail.Mail;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ShsMailRenderer {
    public Mail renderMail(Mail mail, Map<String, String> data) {
        final Mail copy = mail.copy();
        String body = copy.getBody();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            body = body.replace("<<" + entry.getKey() + ">>", entry.getValue());
        }
        copy.setBody(body);
        return copy;
    }
}
