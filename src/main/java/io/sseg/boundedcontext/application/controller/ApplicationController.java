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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/application")
public class ApplicationController {
    
    private final Rq rq;
    private final JwtProvider jwtProvider;
    private final ApplicationService applicationService;
    
    
    // 등록
    @GetMapping("/create")
    public String getApplicationRegisterForm(Model model){
        
        model.addAttribute("form", new ApplicationRegistrationDto());
        
        return "/user/application/create";
    }
    
    // 등록 proc
    @PostMapping("/create")
    @ResponseBody
    public String createApplication(@Valid ApplicationRegistrationDto applicationRegistrationForm){
        
        String appId = Ut.generator.generateUUID();
        String appSecret = Ut.generator.generateRandomString();
        
        Long applicationId = applicationService.create(applicationRegistrationForm, appId, appSecret);
        
        
        return rq.alert("애플리케이션이 등록되었습니다.", "/application");
    }
    
    
    // 리스트
    @GetMapping("")
    public String getApplicationList(Model model){
        
        List<Application> applications = applicationService.findAllByOwnerId(rq.getAccountPrincipal().getId());
        
        model.addAttribute("applications", applications);
        
        return "/user/application/list";
    }
    
    // 상세
    @GetMapping("/{appId}")
    public String getApplicationDetail(@PathVariable String appId, Model model){
        
        Application application = applicationService.findByAppId(appId);
        List<Application> applications = applicationService.findAllByOwnerId(rq.getAccountPrincipal().getId());
        
        model.addAttribute("applications", applications);
        model.addAttribute("app", application);
        
        
        return "/user/application/detail";
    }
    
    
}
