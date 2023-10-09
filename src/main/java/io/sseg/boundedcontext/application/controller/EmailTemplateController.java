package io.sseg.boundedcontext.application.controller;

import io.sseg.base.request.Rq;
import io.sseg.boundedcontext.application.entity.Application;
import io.sseg.boundedcontext.application.entity.EmailTemplate;
import io.sseg.boundedcontext.application.exception.TemplateParsingException;
import io.sseg.boundedcontext.application.model.EmailTemplateDto;
import io.sseg.boundedcontext.application.service.ApplicationService;
import io.sseg.boundedcontext.application.service.JsonToMapConverter;
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
    
    // 템플릿 삭제
    @GetMapping("/{appId}/template/delete")
    @ResponseBody
    public String getApplicationDetail(@PathVariable String appId, @RequestParam String templateName){
        
        boolean isSuccess = applicationService.removeEmailTemplate(appId, templateName);
        
        
        return isSuccess ? rq.historyBack("템플릿이 삭제되었습니다.") : rq.historyBack("템플릿 삭제에 실패했습니다. 다시 시도해주세요.");
    }
    
    // 템플릿 수정 폼
    @GetMapping("/{appId}/template/edit")
    public String editApplication(@PathVariable String appId, @RequestParam String templateName, Model model){
        
        Application application = applicationService.findByAppId(appId);
        EmailTemplate template = applicationService.getEditableEmailTemplate(appId, templateName);
        model.addAttribute("template", template);
        model.addAttribute("sampleVariablesJson", new JsonToMapConverter().convertToDatabaseColumn(template.getSampleVariables()));
        
        return "/user/application/template/edit";
    }
    
    // 템플릿 수정
    @PostMapping("/{appId}/template/edit")
    @ResponseBody
    public ResponseEntity<String> editApplication(@PathVariable String appId, @RequestParam String templateName, @Valid @RequestBody EmailTemplateDto templateForm){
        
        boolean isSuccess = applicationService.editEmailTemplate(appId, templateName, templateForm);
        
        try {
            // 파싱 시도후, 파싱 과정에서 예외가 발생한다면 수정 실패
            templateResolver.resolveHtml(templateForm.getTemplate(), templateForm.getSampleVariables(), templateForm.getVariableNames());
        } catch (TemplateParsingException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        
        if(!isSuccess){
            return ResponseEntity.badRequest().body("해당 템플릿 이름은 이미 등록되어있습니다.");
        } else {
            return ResponseEntity.ok("/application/" + appId);
        }
    }
    
    
//
//    @PostMapping("/{appId}/delete")
//    public String deleteApplication(){
//
//        return "redirect:/application";
//    }
//
    
}
