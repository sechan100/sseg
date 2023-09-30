package io.sseg.boundedContext.user.account.controller;


import io.sseg.base.request.Rq;
import io.sseg.boundedContext.user.account.model.dto.AwaitingEmailVerifyingRedisEntity;
import io.sseg.boundedContext.user.account.model.dto.AccountDetailsRegisterForm;
import io.sseg.boundedContext.user.account.model.dto.VerifyRequestRegisterForm;
import io.sseg.boundedContext.user.account.repository.AwaitingEmailVerifyingFormRepository;
import io.sseg.boundedContext.user.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class RegisterController {
    
    private final Rq rq;
    private final AccountService accountService;
    private final AwaitingEmailVerifyingFormRepository emailCacheRepository;
    
    
    
    @GetMapping("/register")
    public String registerPage(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String email,
            Model model
    ) {
        if(code != null && email != null){
            AwaitingEmailVerifyingRedisEntity verifiedForm = emailCacheRepository.findById(email).orElseThrow();
            AccountDetailsRegisterForm form = new AccountDetailsRegisterForm(verifiedForm);
            model.addAttribute("registerForm", form);
            return "/user/account/verified_register";
        }
        
        
        return "/user/account/register";
    }
    
    
    @PostMapping("/verify/email")
    public String verifyEmail(VerifyRequestRegisterForm form) {
        
        // form 유효성 검사
        if(!accountService.validate(form)){
            throw new IllegalArgumentException("유효하지 않은 입력값입니다.");
        }
        
        // 기존에 이메일 인증 요청을 보낸 기록이 있는지 확인
        boolean isExistInCache = emailCacheRepository.existsById(form.getEmail());
        
        
        String authCode;
        
        
        // 기존에 발급한 이메일 인증코드가 있는지 확인하여 있는 경우, 기존 인증코드를 캐쉬에서 불러오기
        if(isExistInCache){
            authCode = emailCacheRepository.findById(form.getEmail()).orElseThrow().getAuthenticationCode();
        } else {
            authCode = null;
        }
        
        
        // 이메일로 인증코드 발급
        authCode = accountService.sendEmailVerifyingEmail(form.getEmail(), authCode);
        
        
        
        if(!isExistInCache) {
            // redis 캐쉬에 회원가입 정보 임시저장
            emailCacheRepository.save(new AwaitingEmailVerifyingRedisEntity(form, authCode));
        }
        
        return "/user/account/email_verification_waiting";
    }
    
    
    @PostMapping("/register")
    public String register(AccountDetailsRegisterForm form){
        
        // form 유효성 검사
        // 나중에 하자..
        
        
        
        
        return null;
    }
}
