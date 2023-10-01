package io.sseg.boundedcontext.email;

import io.sseg.boundedcontext.email.model.EmailRequest;
import io.sseg.boundedcontext.email.model.EmailResponse;
import io.sseg.boundedcontext.email.service.EmailSendService;
import io.sseg.boundedcontext.email.service.ThymeleafEmailTemplateResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EmailEndPoint {
    
    private final EmailSendService emailService;
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
        
        
        String emailContent = templateResolver.resolveHtml(template, variables);
        
        EmailRequest request = EmailRequest.builder()
                .from("sechan@sseg.io")
                .to("sechan100@gmail.com")
                .subject("테스트 이메일 입니다.")
                .text(emailContent)
                .build();
        
        
        emailService.sendMail(request);
        
        
        EmailResponse response = EmailResponse.builder()
                .status(EmailEnums.Status.SUCCESS)
                .errorCode(EmailEnums.ErrorCode.NO_ERROR)
                .build();
        
        
        return response;
    }
    
    
}
