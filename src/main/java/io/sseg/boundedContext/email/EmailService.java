package io.sseg.boundedContext.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    
    public void sendMail(EmailRequest request) throws MessagingException {
        
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
            throw new MessagingException();
            
        } catch (MailException e) {
            log.info("Fail To Send Email For Authentication!");
        }
    }
}