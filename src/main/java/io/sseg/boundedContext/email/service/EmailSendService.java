package io.sseg.boundedContext.email.service;

import io.sseg.boundedContext.email.model.EmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSendService {
    
    private final JavaMailSender javaMailSender;
    
    
    public void sendMail(EmailRequest request) {
        
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        
        
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(request.getTo());
            mimeMessageHelper.setSubject(request.getSubject());
            mimeMessageHelper.setFrom(request.getFrom());
            mimeMessageHelper.setText(request.getText(), true);
//            mimeMessageHelper.setText(setContext(authKey, type), true); // html page
            
            // ### mail send ###
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
            log.info("Success To Send Email!");
            
            
        } catch (MessagingException e) {
            log.info("Fail To Send Email!");
            
        } catch (MailException e) {
            log.info("Fail To Send Email For Authentication!");
        }
    }
}