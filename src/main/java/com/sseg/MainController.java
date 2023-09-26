package com.sseg;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MainController {
    
    private final EmailService emailService;
    
    @GetMapping("/")
    @ResponseBody
    public String index() {

        
        
        // 이메일 발송
        EmailMsg emailMessage = EmailMsg.builder().to("yosechan100@gmail.com").subject("sseg test web email send").build();
        try {
            
            emailService.sendEmailAuthenticationMail(emailMessage);
            
        } catch(MessagingException e) {
            throw new RuntimeException(e);
        }
        
        
        return "이메일 전송완료!";
    }
}
