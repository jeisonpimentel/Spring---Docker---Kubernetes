package com.company.springcloud.msvc.usuarios.servicesImpl;

import com.company.springcloud.msvc.usuarios.models.entity.EmailRequest;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailServiceImpl {

    private final static String EMAIL_FROM  = "je.pimentel@duocuc.cl";
    private final static String EMAIL_REPLY = "je.pimentel@duocuc.cl";
    private final static String CONTENT     = "text/plain";
    private final static String END_POINT   = "mail/send";

    @Autowired
    SendGrid sendGrid;

    public Response sendEmail(EmailRequest emailRequest ){

        Mail mail = new Mail( new Email( EMAIL_FROM ), emailRequest.getSubject(), new Email( emailRequest.getTo()),
                              new Content(CONTENT, emailRequest.getBody() ) );
        mail.setReplyTo( new Email( EMAIL_REPLY ));
        Request request = new Request();

        Response response = null;

        try {

            request.setMethod( Method.POST );
            request.setEndpoint( END_POINT );
            request.setBody( mail.build() );
            response = this.sendGrid.api( request );

        } catch ( Exception ex ) {
            System.out.println( ex.getMessage() );
        }

        return response;
    }
}
