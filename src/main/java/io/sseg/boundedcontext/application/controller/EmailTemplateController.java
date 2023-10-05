package io.sseg.boundedcontext.application.controller;

import io.sseg.base.request.Rq;
import io.sseg.boundedcontext.application.model.EmailTemplateDto;
import io.sseg.boundedcontext.application.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/application")
public class EmailTemplateController {
    
    private final ApplicationService applicationService;
    private final Rq rq;
    
    @GetMapping("/{appId}/template/create")
    public String getEmailTemplateRegisterForm(@PathVariable String appId, Model model){
        
        model.addAttribute("templateForm", new EmailTemplateDto());
        
        return "/user/application/create";
    }
    
    @PostMapping("/{appId}/template/create")
    public String createApplication(@PathVariable String appId, @Valid EmailTemplateDto templateForm){
        
        boolean isSuccess = applicationService.addEmailTemplate(appId, templateForm);
        
        if(!isSuccess){
            return rq.historyBack("해당 템플릿 이름은 이미 등록되어있습니다.");
        } else {
            return "redirect:/application/" + appId;
        }

    }
    
//
//    @GetMapping("")
//    public String getApplicationList(){
//
//        return "/user/application/list";
//    }
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
