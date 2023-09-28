package io.sseg.boundedContext.user.auth.service;

import io.sseg.boundedContext.user.auth.model.GoogleUser;
import io.sseg.boundedContext.user.auth.model.NaverUser;
import io.sseg.boundedContext.user.auth.model.ProviverUser;
import io.sseg.boundedContext.user.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    
    private final AccountService accountService;
    
    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        
        // social login provider 별로 인증객체를 분리하여 처리
        ProviverUser proviverUser = providerUser(oAuth2User, clientRegistration);
        
        accountService.register(proviverUser, userRequest);
        
        return oAuth2User;
    }
    
    
    // 표준화 되지 않은 OAtuh2User를 리소스서버별로 특화된 ProviderUser 구현체로 변환
    private ProviverUser providerUser(OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        
        String registrationId = clientRegistration.getRegistrationId();
        
        if (registrationId.equals("naver")) {
            
            return new NaverUser(oAuth2User, clientRegistration);
            
        } else if (registrationId.equals("google")) {
            
            return new GoogleUser(oAuth2User, clientRegistration);
            
        } else {
            
            /* 하지만 DefaultOAuth2UserService의 loadUser 메서드에서
             이미 OAuth2AuthenticationException로 모든 예외를 처리하고있기 때문에 아래 예외는 발생하지 않는다. */
            throw new IllegalArgumentException("지원하지 않는 소셜 로그인입니다.");
            
        }
    }
}
