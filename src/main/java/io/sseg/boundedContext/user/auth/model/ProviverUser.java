package io.sseg.boundedContext.user.auth.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Map;


/**
 * 소셜 로그인 리소스서버별로 제공하는 표준화되지 않은 유저 정보를 표준화하기 위한 인터페이스
 */
public interface ProviverUser {
    
    String getId();
    String getUsername();
    String getPassword();
    String getEmail();
    String getProvider();
    List<? extends GrantedAuthority> getAuthorities();
    Map<String, Object> getAttributes();
    
    
    
    
    
}
