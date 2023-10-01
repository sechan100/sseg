package io.sseg.boundedcontext.user.application.controller;


import io.sseg.base.request.Rq;
import io.sseg.boundedcontext.user.application.entity.Application;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ApplicationController {
    
    private final Rq rq;
    
    @GetMapping("/application/{applicationId}")
    public String application(@PathVariable Long applicationId){
        
        rq.ownerShipCheck(applicationId, Application.class);
        
        
        
        return "redirect:/";
    }
    
    
    
    
}
