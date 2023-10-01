package io.sseg.base.request;

import io.sseg.base.entity.BaseEntity;
import io.sseg.base.entity.UserOwnable;
import io.sseg.boundedcontext.user.account.entity.Account;
import io.sseg.boundedcontext.user.account.service.AccountService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;


@Component
@RequestScope
@Slf4j
public class Rq {

    private ApplicationContext context;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private Account account;
    private boolean login = false;
    
    
    public Rq(HttpServletRequest req, HttpServletResponse resp, HttpSession session, ApplicationContext context, AccountService accountService) {
        this.request = req;
        this.response = resp;
        this.session = session;
        this.context = context;
        
        this.account = accountService.findByUsername("seryoung100@naver.com");
        
    }
    
    
    public Cookie getCookie(String name){
        
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }
    
    
    public String alert(String msg, String redirectUrl){
        return "<script>alert('" + msg + "'); location.href='" + redirectUrl + "';</script>";
    }
    
    
    public String historyBack(String msg){
        return "<script>alert('" + msg + "'); history.back();</script>";
    }
    
    
    public void redirect(String redirectUrl) {
        try{
            response.sendRedirect(redirectUrl);
        } catch(IOException e){
            log.info("fail to redirect: bean of Rq.java redirect() method");
        }
    }
    
    public <T extends UserOwnable> void ownerShipCheck(Long id, Class<T> clazz) {
        
        JpaRepository<UserOwnable, Long> repository = null;
        
        if(clazz != null) {
            String className = clazz.getSimpleName();
            
            String beanName = Character.toLowerCase(className.charAt(0)) + className.substring(1) + "Repository";
            repository = (JpaRepository<UserOwnable, Long>) context.getBean(beanName);
        }
        
        Assert.notNull(repository, "소유권 확인 대상 엔티티의 레포지토리가 존재하지 않습니다. repository 빈이 존재하지 않거나, repository 빈의 이름이 네이밍 컨벤션에 부합하지 않을 수 있습니다. (네이밍 컨벤션: 엔티티 이름의 첫 글자를 소문자로 바꾸고, 엔티티 이름 + Repository)");
        UserOwnable targetEntity = repository.findById(id).orElseThrow();
        
        
        if (targetEntity.getOwner() != null) {
            boolean isBelongTo = Objects.equals(targetEntity.getOwner().getId(), this.account.getId());
            if(!isBelongTo){
                throw new AccessDeniedException("접근권한이 없습니다.");
            }
        } else {
            throw new NoSuchElementException("소유권 확인 대상 엔티티의 소유자가 존재하지 않습니다.");
        }
        
    }
}