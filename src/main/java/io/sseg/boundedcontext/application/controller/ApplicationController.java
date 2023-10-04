package io.sseg.boundedcontext.application.controller;


import io.sseg.base.jwt.JwtProvider;
import io.sseg.base.request.Rq;
import io.sseg.boundedcontext.application.entity.Application;
import io.sseg.boundedcontext.application.model.ApplicationRegistrationDto;
import io.sseg.boundedcontext.application.service.ApplicationService;
import io.sseg.infra.util.Ut;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ApplicationController {
    
    private final Rq rq;
    private final JwtProvider jwtProvider;
    private final ApplicationService applicationService;
    
    
    // 등록
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
        String encodedAppSecret = Ut.passwordEncoder.encode(appSecret);
        
        Long applicationId = applicationService.create(applicationRegistrationForm, appId, encodedAppSecret);
        
        
        return "redirect:/application";
    }
    
    
    // 리스트
    @GetMapping("/application")
    public String getApplicationList(Model model){
        
        List<Application> applications = applicationService.findAllByOwnerId(rq.getAccountPrincipal().getId());
        
        model.addAttribute("applications", applications);
        
        return "/user/application/list";
    }
    
    // 상세 (권한 체크에 오류있음)
    @GetMapping("/application/{applicationId}")
    public String getApplicationDetail(@PathVariable Long applicationId, Model model){
        
        rq.isAccessAllowed(applicationId, Application.class);
        
        model.addAttribute("application", applicationService.findById(applicationId));
        
        
        return "/user/application/detail";
    }
    
    
}
