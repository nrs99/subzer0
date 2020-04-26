package edu.lehigh.cse216.slj222.admin;

import com.sendgrid.*;
import java.io.IOException;

public class SendGridEmail {

    private static final SendGrid sg = new SendGrid(
            "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");

    private SendGridEmail() {
    } // Don't have constructor

    public static void sendEmail(String email, String subject, String myContent) {
        Email from = new Email("subzer0.cse216@gmail.com");
        Email to = new Email(email);
        Content content = new Content("text/plain", myContent);
        Mail mail = new Mail(from, subject, to, content);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}