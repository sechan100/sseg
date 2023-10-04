package io.sseg.boundedcontext.application.controller;


import io.sseg.base.jwt.JwtUtil;
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
    private final JwtUtil jwtUtil;
    private final ApplicationService applicationService;
    
    
    @GetMapping("/application/create")
    public String showApplicationRegistrationForm(Model model){
        
        model.addAttribute("form", new ApplicationRegistrationDto());
        
        return "/user/application/create";
    }
    
    
    @PostMapping("/application/create")
    @ResponseBody
    public String createApplication(@Valid ApplicationRegistrationDto applicationRegistrationForm){
        
        String appId = Ut.generator.generateUUID();
        String appSecret = Ut.generator.generateAppSecret();
        String encodedAppSecret = Ut.passwordEncoder.encode(appSecret);
        
        Long applicationId = applicationService.create(applicationRegistrationForm, appId, encodedAppSecret);
        
        
        return "redirect:/application";
    }
    
    
    
    @GetMapping("/application")
    public String showApplicationList(Model model){
        
        List<Application> applications = applicationService.findAllByOwnerId(rq.getAccountPrincipal().getId());
        
        model.addAttribute("applications", applications);
        
        return "/user/application/list";
    }
    
    
    @GetMapping("/application/{applicationId}")
    public String application(@PathVariable Long applicationId, Model model){
        
        rq.isAccessAllowed(applicationId, Application.class);
        
        model.addAttribute("application", applicationService.findById(applicationId));
        
        
        return "/user/application/detail";
    }
    
    
}
