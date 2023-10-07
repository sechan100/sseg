package io.sseg.boundedcontext.email.service;

import io.sseg.boundedcontext.application.entity.SMTPProperties;
import io.sseg.boundedcontext.email.model.EmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSendService {
    
    private final JavaMailSender buildInJavaMailSender;
    
    
    public void sendMail(EmailRequest request, String template, SMTPProperties smtpProperties) {
        
        JavaMailSender javaMailSender;
        
        if(smtpProperties == null){
            javaMailSender = buildInJavaMailSender;
        } else {
            javaMailSender = getJavaMailSender(smtpProperties);
        }
        
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        
        
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(request.getTo());
            mimeMessageHelper.setSubject(request.getSubject());
            mimeMessageHelper.setFrom(request.getFrom());
            mimeMessageHelper.setText(template, true);
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
    
    public void sendMail(EmailRequest request, String template) {
        sendMail(request, template, null);
    }
    
    
    public JavaMailSender getJavaMailSender(SMTPProperties smtp) {
        
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost(smtp.getHost());
            mailSender.setPort(smtp.getPort());
            mailSender.setUsername(smtp.getUsername());
            mailSender.setPassword(smtp.getPassword());
        
        Properties props = mailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.debug", "true");
        
        return mailSender;
    }
}