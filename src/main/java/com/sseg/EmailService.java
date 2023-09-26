package com.sseg;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    
    public void sendEmailAuthenticationMail(EmailMsg emailMessage) throws MessagingException {
        
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        
        
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo()); // address
            mimeMessageHelper.setSubject(emailMessage.getSubject()); // subject
//            mimeMessageHelper.setText(setContext(authKey, type), true); // html page
            mimeMessageHelper.setText("""
                    <!doctype html>
                    <html lang="en">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport"
                              content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
                        <meta http-equiv="X-UA-Compatible" content="ie=edge">
                        <title>Document</title>
                    </head>
                    <body>
                        <h1>안녕하세요</h1>
                    </body>
                    </html>    \s
                    """, true); // html page
            mimeMessageHelper.setFrom("sechan@sseg.io");
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
            log.info("Success To Send Email!");
            
        } catch (MessagingException e) {
            throw new MessagingException();
            
        } catch (MailException e) {
            log.info("Fail To Send Email For Authentication!");
        }
    }
    
}