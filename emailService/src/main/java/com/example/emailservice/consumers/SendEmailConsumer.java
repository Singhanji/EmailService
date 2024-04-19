package com.example.emailservice.consumers;

import com.example.emailservice.Services.EmailUtil;
import com.example.emailservice.dtos.sendEmailMessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

// In this EmailServiceConsumer, we will have a consumer that will read a particular message

@Service
public class SendEmailConsumer {
    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(
            id = "emailServiceConsumerGroup",
            topics = {"sendEmail"}
    )
    public void handleSendEmail(String message) throws JsonProcessingException {
//        System.out.println("Got the sent email message!");

        sendEmailMessageDto messageDto = objectMapper.readValue(message, sendEmailMessageDto.class);

//        System.out.println("Will send email to: " + messageDto.getTo());

        Properties props = new  Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("emailservicescalertest@gmail.com", "egfe rxae hmir vktp");
            }
        };
        Session session = Session.getInstance(props, auth);

        EmailUtil.sendEmail(session,
                messageDto.getTo(),
                messageDto.getSubject(),
                messageDto.getBody()
        );

    }
}
