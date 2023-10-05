package io.sseg.boundedcontext.email.controller;

import io.sseg.base.aop.RequireJwtToken;
import io.sseg.base.http.ApiResponse;
import io.sseg.base.http.SsegApiResponseStatus;
import io.sseg.base.request.Rq;
import io.sseg.boundedcontext.application.entity.Application;
import io.sseg.boundedcontext.application.entity.EmailTemplate;
import io.sseg.boundedcontext.application.service.ApplicationService;
import io.sseg.boundedcontext.email.EmailEnums;
import io.sseg.boundedcontext.email.constants.SendingProcessStatus;
import io.sseg.boundedcontext.email.model.ApiEmailRequest;
import io.sseg.boundedcontext.email.model.EmailRequest;
import io.sseg.boundedcontext.email.model.EmailResponse;
import io.sseg.boundedcontext.email.service.EmailSendService;
import io.sseg.boundedcontext.email.service.ThymeleafEmailTemplateResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
@Slf4j
public class ApiEmailController {
    
    private final EmailSendService emailService;
    private final ThymeleafEmailTemplateResolver templateResolver;
    private final ApplicationService applicationService;
    private final Rq rq;
    
    
    // 템플릿 등록 기능 만들기 전까지는, 사용자가 템플릿 선택없이 요청을 보냈다고 가정하고 build-in 템플릿을 사용하여 처리하는 시나리오
    @PostMapping("/send")
    @RequireJwtToken
    public ResponseEntity<ApiResponse<SendingProcessStatus>> sendEmail(@RequestBody ApiEmailRequest request) {

        String appId = rq.getJwtToken().getAppId();
        Application application = applicationService.findByAppId(appId);
        
        String template = String.valueOf(application.getEmailTemplates()
                .stream()
                .filter(t -> t.getName().equals(request.getTemplateName()))
                .map(EmailTemplate::getTemplate)
                .findAny()
        );
        
        
        try {
            template = templateResolver.resolveHtml(template, request.getTemplateArgs());
            
            // thymeleaf exception
        } catch (Exception e) {
            log.error("템플릿 파싱 에러", e);
            return ApiResponse.unprocessableEntity(SsegApiResponseStatus.INTERNAL_SERVER_ERROR);
        }

        
        emailService.sendMail(request.getEmailRequest(), template);
        
        return ApiResponse.ok(new SendingProcessStatus());
        
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
