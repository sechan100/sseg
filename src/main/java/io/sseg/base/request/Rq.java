package io.sseg.base.request;

import io.sseg.boundedContext.user.account.entity.Account;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;


@Component
@RequestScope
@Slf4j
public class Rq {
    
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private Account account;
    private boolean login = false;
    
    
    public Rq(HttpServletRequest req, HttpServletResponse resp, HttpSession session) {
        this.request = req;
        this.response = resp;
        this.session = session;
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
}