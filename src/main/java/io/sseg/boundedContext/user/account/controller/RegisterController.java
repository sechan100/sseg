package io.sseg.boundedContext.user.account.controller;


import io.sseg.base.request.Rq;
import io.sseg.boundedContext.user.account.dto.RegistrationForm;
import io.sseg.boundedContext.user.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class RegisterController {
    
    private final Rq rq;
    private final AccountService accountService;
    
    
    @GetMapping("/register")
    public String registerPage() {
        return "/user/account/register";
    }
    
    
    @PostMapping("/register")
    @ResponseBody
    public String register(RegistrationForm form) {
        
        // form 유효성 검사
        if(!form.validate()){
            return form.historyBack();
        }
        
        
        
        
        
        return rq.alert("회원가입이 완료되었습니다.", "/");
    }
    
}
