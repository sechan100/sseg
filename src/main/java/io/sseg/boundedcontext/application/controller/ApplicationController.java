package io.sseg.boundedcontext.application.controller;


import io.sseg.base.jwt.JwtUtil;
import io.sseg.base.request.Rq;
import io.sseg.boundedcontext.application.entity.Application;
import io.sseg.boundedcontext.application.model.ApplicationDto;
import io.sseg.boundedcontext.application.model.ApplicationRegistrationDto;
import io.sseg.boundedcontext.application.service.ApplicationService;
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
    
    @PostMapping("/application/create")
    @ResponseBody
    public String createApplication(@Valid ApplicationRegistrationDto applicationRegistrationForm){
        
        Long applicationId = applicationService.create(applicationRegistrationForm);
        
        String jwtToken = jwtUtil.generateToken(applicationId);
        
        
        return jwtToken;
    }
    
    
    
    @GetMapping("/application")
    public String showApplicationList(Model model){
        
        List<Application> applications = applicationService.findAllByOwnerId(rq.getAccountPrincipal().getId());
        
        model.addAttribute("applications", applications);
        
        return "/user/application/list";
    }
    
    
    @GetMapping("/application/{applicationId}")
    public String application(@PathVariable Long applicationId){
        
        rq.isAccessAllowed(applicationId, Application.class);
        
        
        
        return "redirect:/";
    }
    
    
    
    
}
