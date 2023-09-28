package io.sseg.boundedContext.email;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class TestEmailController {
    
    private final EmailService emailService;
    
    @GetMapping("/send")
    @ResponseBody
    public String index() {

        
        
        // 이메일 발송
        EmailRequest emailMessage = EmailRequest.builder().to("sechan100@gmail.com").subject("sseg test web email send").build();
        try {
            
            emailService.sendMail(emailMessage);
            
        } catch(MessagingException e) {
            throw new RuntimeException(e);
        }
        
        
        return "이메일 전송완료!";
    }
    
}














