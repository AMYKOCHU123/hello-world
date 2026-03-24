package com.example.service;

import com.example.model.MailRequest;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.*;

import java.util.logging.Logger;

public class MailService {

    private static final Logger logger = Logger.getLogger(MailService.class.getName());
    private final SendGrid sendGrid;

    public MailService() {
        String apiKey = System.getenv("SENDGRID_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("SENDGRID_API_KEY environment variable is not set");
        }
        this.sendGrid = new SendGrid(apiKey);
    }

    public int sendMail(MailRequest mailRequest) throws Exception {
        Email from = new Email("amal.chempazhanthy@gmail.com"); // verified sender
        Email to = new Email(mailRequest.getTo());
        Content content = new Content("text/plain", mailRequest.getContent());
        Mail mail = new Mail(from, mailRequest.getSubject(), to, content);

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        try {
            Response response = sendGrid.api(request);
            logger.info("SendGrid response: status=" + response.getStatusCode());
            return response.getStatusCode();
        } catch (Exception e) {
            logger.severe("Failed to send email: " + e.getMessage());
            throw e;
        }
    }
}