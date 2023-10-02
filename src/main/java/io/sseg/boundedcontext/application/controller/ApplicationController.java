package io.sseg.boundedcontext.application.controller;


import io.sseg.base.request.Rq;
import io.sseg.boundedcontext.application.entity.Application;
import io.sseg.boundedcontext.application.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ApplicationController {
    
    private final Rq rq;
    private final ApplicationService applicationService;
    
    
    @GetMapping("/application")
    public String showApplicationList(Model model){
        
        List<Application> applications = applicationService.showMyApplications();
        
        model.addAttribute("applications", applications);
        
        return "/user/application/list";
    }
    
    
    @GetMapping("/application/{applicationId}")
    public String application(@PathVariable Long applicationId){
        
        rq.isAccessAllowed(applicationId, Application.class);
        
        
        
        return "redirect:/";
    }
    
    
    
    
}
