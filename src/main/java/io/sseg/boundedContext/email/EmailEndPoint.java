package io.sseg.boundedContext.email;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EmailEndPoint {
    
    private final EmailService emailService;
    private final ThymeleafEmailTemplateResolver templateResolver;
    
    
    @GetMapping("/email/send")
    public EmailResponse sendEmail() {
        
        String template = """
                <html>
                    <body>
                        <h1>안녕하세요</h1>
                        <p th:text="${content}"></p>
                    </body>
                </html>
                """;
        
        Map<String, Object> variables = Map.of("content", "테스트 이메일 입니다.");
        
        
        String emailContent = templateResolver.resolve(template, variables);
        
        EmailRequest request = EmailRequest.builder()
                .from("sechan@sseg.io")
                .to("sechan100@gmail.com")
                .subject("테스트 이메일 입니다.")
                .text(emailContent)
                .build();
        
        
        
        try {
            emailService.sendMail(request);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        
        
        EmailResponse response = EmailResponse.builder()
                .status(EmailEnums.Status.SUCCESS)
                .errorCode(EmailEnums.ErrorCode.NO_ERROR)
                .build();
        
        
        return response;
    }
    
    
}
