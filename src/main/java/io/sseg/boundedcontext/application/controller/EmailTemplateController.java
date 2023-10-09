package io.sseg.boundedcontext.application.controller;

import io.sseg.base.request.Rq;
import io.sseg.boundedcontext.application.exception.TemplateParsingException;
import io.sseg.boundedcontext.application.model.EmailTemplateDto;
import io.sseg.boundedcontext.application.service.ApplicationService;
import io.sseg.boundedcontext.email.service.ThymeleafEmailTemplateResolver;
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
@RequestMapping("/application")
public class EmailTemplateController {
    
    private final ApplicationService applicationService;
    private final ThymeleafEmailTemplateResolver templateResolver;
    private final Rq rq;
    
    // 템플릿 등록 폼
    @GetMapping("/{appId}/template/create")
    public String getEmailTemplateRegisterForm(@PathVariable String appId, Model model){
        
        model.addAttribute("appId", appId);
        
        return "/user/application/template/create";
    }
    
    // 템플릿 등록
    @PostMapping("/{appId}/template/create")
    @ResponseBody
    public ResponseEntity<String> createApplication(@PathVariable String appId, @Valid @RequestBody EmailTemplateDto templateForm){
        
        boolean isSuccess = applicationService.addEmailTemplate(appId, templateForm);
        
        if(!isSuccess){
            return ResponseEntity.badRequest().body("해당 템플릿 이름은 이미 등록되어있습니다.");
        } else {
            return ResponseEntity.ok("/application/" + appId);
        }

    }
    

    @PostMapping("/ajax/resolve/template")
    @ResponseBody
    public ResponseEntity<String> getApplicationList(@RequestBody Map<String, Object> data) {
        
        String response;
        
        String template = (String) data.get("template");
        Map<String, Object> sampleVariables = (Map<String, Object>) data.get("variables");
        List<String> variableNames = (List<String>) data.get("variableNames");
        
        try {
            response = templateResolver.resolveHtml(template, sampleVariables, variableNames);
            return ResponseEntity.ok(response);
            
        } catch (TemplateParsingException e) {
            response = e.getMessage();
            return ResponseEntity.internalServerError().body(response);
        }
        
    }
    
    
//
//    @GetMapping("/{appId}")
//    public String getApplicationDetail(){
//
//        return "/user/application/detail";
//    }
//
//    @PostMapping("/{appId}/edit")
//    public String editApplication(){
//
//        return "redirect:/application";
//    }
//
//    @PostMapping("/{appId}/delete")
//    public String deleteApplication(){
//
//        return "redirect:/application";
//    }
//
    
}
