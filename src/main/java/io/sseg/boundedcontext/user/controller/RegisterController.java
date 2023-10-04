package io.sseg.boundedcontext.user.controller;


import io.sseg.base.request.Rq;
import io.sseg.base.validation.validator.AccountDtoValidator;
import io.sseg.boundedcontext.user.model.dto.AccountDetailsRegisterForm;
import io.sseg.boundedcontext.user.model.dto.AwaitingEmailVerifyingRedisEntity;
import io.sseg.boundedcontext.user.service.AccountService;
import io.sseg.boundedcontext.user.service.AwaitingEmailVerifyingFormService;
import io.sseg.boundedcontext.user.model.dto.VerifyRequestRegisterForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class RegisterController {
    
    private final Rq rq;
    private final AccountService accountService;
    private final AwaitingEmailVerifyingFormService awaitingEmailVerifyingFormService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(new AccountDtoValidator(accountService.getAccountRepository()));
    }
    
    
    @GetMapping("/register")
    public String registerPage(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String email,
            Model model
    ) {
        if(code != null && email != null){
            AwaitingEmailVerifyingRedisEntity verifiedForm = awaitingEmailVerifyingFormService.findById(email);
            AccountDetailsRegisterForm form = new AccountDetailsRegisterForm(verifiedForm);
            model.addAttribute("registerForm", form);
            return "/user/account/verified_register";
        }
        
        
        return "/user/account/register";
    }
    
    
    @PostMapping("/verify/email")
    public String verifyEmail(@Valid VerifyRequestRegisterForm form, Model model) {
        
        // 기존에 이메일 인증 요청을 보낸 기록이 있는지 확인
        boolean isExistInCache = awaitingEmailVerifyingFormService.existsById(form.getEmail());
        
        
        String authCode;
        
        
        // 기존에 발급한 이메일 인증코드가 있는지 확인하여 있는 경우, 기존 인증코드를 캐쉬에서 불러오기
        if(isExistInCache){
            authCode = awaitingEmailVerifyingFormService.findById(form.getEmail()).getAuthenticationCode();
        } else {
            authCode = null;
        }
        
        
        // 이메일로 인증코드 발급
        authCode = accountService.sendEmailVerifyingEmail(form.getEmail(), authCode);
        
        
        
        if(!isExistInCache) {
            // redis 캐쉬에 회원가입 정보 임시저장
            awaitingEmailVerifyingFormService.save(new AwaitingEmailVerifyingRedisEntity(form, authCode));
        }
        
        return "/user/account/email_verification_waiting";
    }
    
    
    @PostMapping("/register")
    @ResponseBody
    public String register(@Valid AccountDetailsRegisterForm form){
        
        accountService.register(form);
        
        awaitingEmailVerifyingFormService.delete(form.getEmail());
        
        
        return rq.alert("회원가입이 완료되었습니다.", "/login");
    }
}
