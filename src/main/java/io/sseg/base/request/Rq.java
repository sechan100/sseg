package io.sseg.base.request;

import io.sseg.base.entity.UserOwnable;
import io.sseg.boundedcontext.user.entity.Account;
import io.sseg.boundedcontext.user.model.dto.AccountPrincipal;
import io.sseg.boundedcontext.user.model.oauth.PrincipalContext;
import io.sseg.boundedcontext.user.service.AccountService;
import io.sseg.base.security.util.Role;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Objects;


@Component
@RequestScope
@Getter
@Slf4j
public class Rq {

    private final ApplicationContext context;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final HttpSession session;
    private final AccountPrincipal accountPrincipal;
    private final AccountService accountService;
    
    
    public Rq(HttpServletRequest req, HttpServletResponse resp, HttpSession session, ApplicationContext context, AccountService accountService) {
        this.request = req;
        this.response = resp;
        this.session = session;
        this.context = context;
        this.accountService = accountService;
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        
        if(authentication instanceof UsernamePasswordAuthenticationToken) {
            
            AccountPrincipal user = ((PrincipalContext)authentication.getPrincipal()).getAccount();
            user.setLogin(true);
            this.accountPrincipal = user;
            
        } else if(authentication instanceof OAuth2AuthenticationToken) {
            
            AccountPrincipal user = ((PrincipalContext)authentication.getPrincipal()).getAccount();
            String nickname = user.getNickname();
            
            // OAuth를 이용해서 신규로 가입하는 사용자인 경우
            if(nickname == null){
                
                this.accountPrincipal = new AccountPrincipal(Account.builder().username("anonymous").nickname("anonymous").role(Role.ANONYMOUS).build());
                
                
            // OAuth를 통한 로그인인 경우
            } else {
                user.setLogin(true);
                this.accountPrincipal = user;
            }
            
        } else if(authentication instanceof AnonymousAuthenticationToken) {
            
            this.accountPrincipal = new AccountPrincipal(Account.builder().username("anonymous").nickname("anonymous").role(Role.ANONYMOUS).build());
            
        } else {
            
            this.accountPrincipal = null;
            
        }
    }
    
    
    public void setRequestAttr(String attrName, Object attrValue) {
        request.setAttribute(attrName, attrValue);
    }
    
    public <R> R getRequestAttr(String attrName) {
        return (R) request.getAttribute(attrName);
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
    
    public <T extends UserOwnable> void isAccessAllowed(Long id, Class<T> clazz) {
        
        JpaRepository<UserOwnable, Long> repository = null;
        
        if(id == null) {
            throw new NullPointerException("소유권 확인 대상 엔티티의 id값이 존재하지 않습니다.");
        }
        
        if(clazz != null) {
            String className = clazz.getSimpleName();
            
            String beanName = Character.toLowerCase(className.charAt(0)) + className.substring(1) + "Repository";
            repository = (JpaRepository<UserOwnable, Long>) context.getBean(beanName);
        }
        
        if(repository == null) {
            throw new NullPointerException("소유권 확인 대상 엔티티의 레포지토리가 존재하지 않습니다. repository 빈이 존재하지 않거나, repository 빈의 이름이 네이밍 컨벤션에 부합하지 않을 수 있습니다. (네이밍 컨벤션: 엔티티 이름의 첫 글자를 소문자로 바꾸고, 엔티티 이름 + Repository");
        }
        
        UserOwnable targetEntity = repository.findById(id).orElseThrow(() -> new NoSuchElementException("소유권 확인 대상 엔티티가 존재하지 않습니다."));
        
        
        if (targetEntity.getOwner() != null) {
            
            boolean isUserOwner = Objects.equals(targetEntity.getOwner().getId(), this.accountPrincipal.getId());
            boolean isAdmin = this.accountPrincipal.isAdmin();
            
            if(!isUserOwner || !isAdmin){
                throw new AccessDeniedException("접근권한이 없습니다.");
            }
            
        } else {
            throw new NoSuchElementException("소유권 확인 대상 엔티티의 소유자가 존재하지 않습니다.");
        }
        
    }
    
    
    public Account getAccount() {
        return accountService.findByUsername(accountPrincipal.getUsername());
    }
}