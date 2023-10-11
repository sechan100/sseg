package io.sseg.boundedcontext.email.controller;

import io.sseg.base.aop.RequireJwtToken;
import io.sseg.base.http.ApiResponse;
import io.sseg.base.http.SsegApiResponseStatus;
import io.sseg.base.request.Rq;
import io.sseg.boundedcontext.application.entity.Application;
import io.sseg.boundedcontext.application.entity.EmailTemplate;
import io.sseg.boundedcontext.application.exception.TemplateParsingException;
import io.sseg.boundedcontext.application.service.ApplicationService;
import io.sseg.boundedcontext.email.model.ApiEmailRequest;
import io.sseg.boundedcontext.email.service.EmailSendService;
import io.sseg.boundedcontext.email.service.ThymeleafEmailTemplateResolver;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<ApiResponse<SsegApiResponseStatus>> sendEmail(@RequestBody ApiEmailRequest request) {
        
        String appId = rq.getJwtToken().getAppId();
        Application application = applicationService.findByAppId(appId);
        
        // 이메일 전송자 이름을 '요청 받은 전송자 이름@도메인' 형식으로 변환
        request.getEmailRequest().setFrom(request.getEmailRequest().getFrom() + "@" + application.getDomain());
        
        // 사용자 애플리케이션의 템플릿 목록에서 요청에 담긴 템플릿 이름으로 템플릿을 찾는다.
        String template = application.getEmailTemplates()
                .stream()
                .filter(t -> t.getName().equals(request.getTemplateName()))
                .map(EmailTemplate::getTemplate)
                .findAny().orElse(null);
        
        
        // error: 요청 받은 템플릿 이름으로 템플릿을 찾지 못했을 경우
        if(template == null) {
            return ApiResponse.unprocessableEntity(SsegApiResponseStatus.TEMPLATE_NOT_FOUND, String.format("Requeted template name '%s' maybe not exist or invalid template name. check your registered template name, and Compare to requeted template name.", request.getTemplateName()));
        }
        
        
        // 사용자가 등록한 '템플릿에 필요한 변수 이름 목록' 얻어오기
        List<String> variableNames = application.getEmailTemplates()
                .stream()
                .filter(t -> t.getName().equals(request.getTemplateName()))
                .flatMap(t -> t.getVariableNames().stream())
                .collect(Collectors.toList());
        
        
        try {
            
            // 템플릿, 템플릿 변수, 템플릿 변수 목록을 사용하여 템플릿 파싱
            template = templateResolver.resolveHtml(template, request.getTemplateArgs(), variableNames);
            
        // thymeleaf exception
        } catch (TemplateParsingException e) {
            return ApiResponse.unprocessableEntity(SsegApiResponseStatus.TEMPLATE_PARSING_ERROR, e.getMessage());
            
        } catch(Exception e){
            e.getStackTrace();
            return ApiResponse.unprocessableEntity(SsegApiResponseStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            // 이메일 요청정보와 파싱된 템플릿, 애플리케이션에 등록된 smtp 서버 정보를 사용하여 이메일을 전송
            emailService.sendMail(request.getEmailRequest(), template, application.getSmtpProperties());
            return ApiResponse.ok(SsegApiResponseStatus.SUCCESS);
            
        } catch(MessagingException | MailException e){
            return ApiResponse.unprocessableEntity(SsegApiResponseStatus.INTERNAL_SERVER_ERROR, "internal server error msg => '" + e.getMessage() +"'");
        }
        
    }
    
    
}
