package io.sseg.boundedContext.user.account.dto;


import io.sseg.base.request.Rq;
import io.sseg.boundedContext.user.account.service.AccountService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
public class RegistrationForm {
    
    private String username;
    private String password;
    private String passwordConfirm;
    private String email;
    
    
    @Autowired
    private final Rq rq;
    private AccountService accountService;
    
    
    
    public boolean validate(){
        
        // username 중복 검사
        if(accountService.isExistUsername(username)){
            return false;
        }
        
        // password 일치 검사
        if(!password.equals(passwordConfirm)){
            return false;
        }
        
        // email 중복 검사
        if(accountService.isExistEmail(email)){
            return false;
        }
        
        return true;
    }
    
    
    public String historyBack(){
        
        // username 중복 검사
        if(accountService.isExistUsername(username)){
            return rq.historyBack("이미 사용중인 아이디입니다.");
        }
        
        // password 일치 검사
        if(!password.equals(passwordConfirm)){
            return rq.historyBack("비밀번호가 일치하지 않습니다.");
        }
        
        // email 중복 검사
        if(accountService.isExistEmail(email)){
            return rq.historyBack("이미 사용중인 이메일입니다.");
        }
        
        return null;
    }
    
    
    
}
