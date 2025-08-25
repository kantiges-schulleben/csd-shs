package com.klnsdr.axon.mail.service;

import com.klnsdr.axon.mail.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class MailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);
    private final BlockingQueue<Mail> mailQueue = new LinkedBlockingQueue<>();

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(Mail mail) {
        try {
            mailQueue.add(mail);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Scheduled(fixedRate = 10000)
    public void processEmailQueue() {
        int batchSize = 5;
        for (int i = 0; i < batchSize; i++) {
            final Mail mail = mailQueue.poll();
            if (mail == null) break;

            try {
                LOGGER.debug("sending email to {}", mail.getTo());
                LOGGER.debug("subject: {}", mail.getSubject());
                LOGGER.debug("body: {}", mail.getBody());

                final SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(mail.getFrom());
                message.setTo(mail.getTo());
                message.setSubject(mail.getSubject());
                message.setText(mail.getBody());
                mailSender.send(message);
            } catch (Exception e) {
                LOGGER.error("Failed to send email to {}: {}", mail.getTo(), e.getMessage(), e);
            }
        }
    }
}
