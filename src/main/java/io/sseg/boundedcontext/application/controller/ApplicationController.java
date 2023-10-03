package io.sseg.boundedcontext.application.controller;


import io.sseg.base.request.Rq;
import io.sseg.boundedcontext.application.entity.Application;
import io.sseg.boundedcontext.application.model.ApplicationDto;
import io.sseg.boundedcontext.application.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ApplicationController {
    
    private final Rq rq;
    private final ApplicationService applicationService;
    
    @PostMapping("/application/create")
    public String createApplication(@Valid ApplicationDto applicationRegistrationForm){
        
        
        // form 유효성 검사(아이디 중복 검사, null 체크 등)
        
        
        return "";
    }
    
    
    
    @GetMapping("/application")
    public String showApplicationList(Model model){
        
        List<Application> applications = applicationService.findAllByOwnerId(rq.getUser().getId());
        
        model.addAttribute("applications", applications);
        
        return "/user/application/list";
    }
    
    
    @GetMapping("/application/{applicationId}")
    public String application(@PathVariable Long applicationId){
        
        rq.isAccessAllowed(applicationId, Application.class);
        
        
        
        return "redirect:/";
    }
    
    
    
    
}
