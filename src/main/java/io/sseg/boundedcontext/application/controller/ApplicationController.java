package io.sseg.boundedcontext.application.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.sseg.base.jwt.JwtProvider;
import io.sseg.base.request.Rq;
import io.sseg.boundedcontext.application.entity.Application;
import io.sseg.boundedcontext.application.entity.SMTPProperties;
import io.sseg.boundedcontext.application.model.ApplicationRegistrationDto;
import io.sseg.boundedcontext.application.service.ApplicationService;
import io.sseg.infra.util.Ut;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ApplicationController {
    
    private final Rq rq;
    private final JwtProvider jwtProvider;
    private final ApplicationService applicationService;
    
    
    
    // 등록 form
    @GetMapping("/application/create")
    public String getApplicationRegisterForm(Model model){
        
        model.addAttribute("form", new ApplicationRegistrationDto());
        
        return "/user/application/create";
    }
    
    
    // 등록 proc
    @PostMapping("/application/create")
    @ResponseBody
    public String createApplication(@Valid ApplicationRegistrationDto applicationRegistrationForm){
        
        String appId = Ut.generator.generateUUID();
        String appSecret = Ut.generator.generateRandomString();
        
        Long applicationId = applicationService.create(applicationRegistrationForm, appId, appSecret);
        
        
        return rq.alert("애플리케이션이 등록되었습니다.", "/application");
    }
    
    
    // 리스트
    @GetMapping("/application")
    public String getApplicationList(Model model){
        
        List<Application> applications = applicationService.findAllByOwnerId(rq.getAccountPrincipal().getId());
        
        model.addAttribute("applications", applications);
        
        return "/user/application/list";
    }
    
    
    // 상세
    @GetMapping("/application/{appId}")
    public String getApplicationDetail(@PathVariable String appId, Model model){
        
        Application application = applicationService.findByAppId(appId);
        List<Application> applications = applicationService.findAllByOwnerId(rq.getAccountPrincipal().getId());
        
        model.addAttribute("applications", applications);
        model.addAttribute("app", application);
        
        
        return "/user/application/detail";
    }
    
    
    // smtp 서버 정보 수정
    @PostMapping("/ajax/application/{appId}/smtpProperties")
    @ResponseBody
    public ResponseEntity<String> updateApplicationSmtp(@PathVariable String appId, @RequestBody SMTPProperties smtpProperties) throws JsonProcessingException {
        
        if(applicationService.updateSmtp(appId, smtpProperties)){
            return ResponseEntity.ok(new ObjectMapper().writeValueAsString(smtpProperties));
        } else {
            return ResponseEntity.badRequest().body("SMTP 서버 정보 수정에 실패했습니다.");
        }
    }
    
    
    // 애플리케이션 시크릿 키 재발급
    @GetMapping("/ajax/application/{appId}/regenerateAppSecret")
    public ResponseEntity<String> regenerateAppSecret(@PathVariable String appId){
        
        String newAppSecret = Ut.generator.generateRandomString();
        
        if(applicationService.updateAppSecret(appId, newAppSecret)){
            return ResponseEntity.ok(newAppSecret);
        } else {
            return ResponseEntity.badRequest().body("애플리케이션 시크릿 키 재발급에 실패했습니다.");
        }
    }
}
