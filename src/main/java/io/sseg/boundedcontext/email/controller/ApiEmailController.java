package io.sseg.boundedcontext.email.controller;

import io.sseg.base.aop.RequireJwtToken;
import io.sseg.base.request.Rq;
import io.sseg.boundedcontext.email.EmailEnums;
import io.sseg.boundedcontext.email.model.ApiEmailRequest;
import io.sseg.boundedcontext.email.model.EmailRequest;
import io.sseg.boundedcontext.email.model.EmailResponse;
import io.sseg.boundedcontext.email.service.EmailSendService;
import io.sseg.boundedcontext.email.service.ThymeleafEmailTemplateResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
@Slf4j
public class ApiEmailController {
    
    private final EmailSendService emailService;
    private final ThymeleafEmailTemplateResolver templateResolver;
    private final Rq rq;
    
    
    // 템플릿 등록 기능 만들기 전까지는, 사용자가 템플릿 선택없이 요청을 보냈다고 가정하고 build-in 템플릿을 사용하여 처리하는 시나리오
    @PostMapping("/send")
    @RequireJwtToken
    public EmailResponse sendEmail(ApiEmailRequest request) {
        
        // 사용자가 요청한 템플릿 가져오는 코드
        String template = """
                <html>
                    <body>
                        <h1>안녕하세요~~~!! sseg 이메일 전송 테스트 입니다!</h1>
                        
                        앱 아이디는: <span>[[ ${appId} ]]</span>
                        
                        <div th:if="${strSseg} == 'sseg'" th:text="${contentTrue}"></div>
                        <div th:unless="${strSseg} == 'sseg'" th:text="${contentFalse}"></div>
                        
                        <div th:object="${emailRequest}">
                            <span th:text="*{from}"></span>
                            <span th:text="*{to}"></span>
                            <span th:text="*{subject}"></span>
                        </div>
                        
                    </body>
                </html>
                """;
        
        Map<String, Object> args = Map.of("appId", rq.getJwtToken().getAppId(), "contentTrue", "이건 strSseg가 sseg라면 보여야하는 문자열이에요!", "contentFalse", "이건 strSseg가 sseg라면 보이면 안되는 문자열이에요!(내가 보이니..?)", "strSseg", "sseg", "emailRequest", request.getEmailRequest());
        
        template = templateResolver.resolveHtml(template, args);
        
        emailService.sendMail(request.getEmailRequest(), template);
        
        return EmailResponse.builder()
                .status(EmailEnums.Status.SUCCESS)
                .errorCode(EmailEnums.ErrorCode.NO_ERROR)
                .build();
        
    }
    
    
    
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
                .build();
        
        
        emailService.sendMail(request, emailContent);
        
        
        EmailResponse response = EmailResponse.builder()
                .status(EmailEnums.Status.SUCCESS)
                .errorCode(EmailEnums.ErrorCode.NO_ERROR)
                .build();
        
        
        return response;
    }
    
    
}
