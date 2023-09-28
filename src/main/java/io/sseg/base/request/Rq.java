package io.sseg.base.request;

import io.sseg.boundedContext.user.entity.Account;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

@Component
@RequestScope
public class Rq {
    
    @Getter private final HttpServletRequest request;
    @Getter private HttpServletResponse response;
    @Getter private HttpSession session;
    @Getter private Account account;
    private boolean login = false;
    
    
    public Rq(HttpServletRequest req, HttpServletResponse resp, HttpSession session, Authentication authentication) {
        this.request = req;
        this.response = resp;
        this.session = session;
        
        if(authentication != null && authentication.isAuthenticated()){
            this.account = (Account) authentication.getPrincipal();
            login = true;
        }
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
}