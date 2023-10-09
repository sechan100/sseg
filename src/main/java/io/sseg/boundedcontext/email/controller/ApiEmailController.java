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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
@Slf4j
public class ApiEmailController {
    
    private final EmailSendService emailService;
    private final ThymeleafEmailTemplateResolver templateResolver;
    private final ApplicationService applicationService;
    private final Rq rq;
    
    
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
        
        List variableNames = application.getEmailTemplates()
                .stream()
                .filter(t -> t.getName().equals(request.getTemplateName()))
                .map(EmailTemplate::getVariableNames)
                .toList();
        
        
        try {
            template = templateResolver.resolveHtml(template, request.getTemplateArgs(), variableNames);
            
            // thymeleaf exception
        } catch (Exception e) {
            log.error("템플릿 파싱 에러", e);
            return ApiResponse.unprocessableEntity(SsegApiResponseStatus.INTERNAL_SERVER_ERROR);
        }

        
        emailService.sendMail(request.getEmailRequest(), template);
//        emailService.sendMail(request.getEmailRequest(), template, application.getSmtpProperties());
        
        return ApiResponse.ok(new SendingProcessStatus());
        
    }
    
    
    
}
