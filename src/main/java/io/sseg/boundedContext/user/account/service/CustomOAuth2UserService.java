package io.sseg.boundedContext.user.account.service;

import io.sseg.base.request.Rq;
import io.sseg.boundedContext.user.account.model.dto.AccountDetailsRegisterForm;
import io.sseg.boundedContext.user.account.model.dto.AwaitingEmailVerifyingRedisEntity;
import io.sseg.boundedContext.user.account.model.oauthuser.GoogleUser;
import io.sseg.boundedContext.user.account.model.oauthuser.NaverUser;
import io.sseg.boundedContext.user.account.model.oauthuser.ProviderUser;
import io.sseg.infra.Ut;
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
    private final AwaitingEmailVerifyingFormService emailCacheService;
    private final Rq rq;
    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        
        // social login provider 별로 인증객체를 분리하여 처리
        ProviderUser proviverUser = providerUser(oAuth2User, clientRegistration);
        AccountDetailsRegisterForm registerForm = new AccountDetailsRegisterForm(proviverUser);
        
        
        // 기존 로그인 정보가 있다면 회원정보를 업데이트하고 그대로 로그인 진행
        if(accountService.isExistUsername(registerForm.getUsername())){
            
            accountService.updateAccountDetails(registerForm);
            
            
        // 기존 로그인 정보가 없다면 기본 Account 정보가 포함된 AccountDetilasRegisterForm을 가지고 회원가입 진행
        } else {
            String authenticationCode = Ut.randomString();
            emailCacheService.save(new AwaitingEmailVerifyingRedisEntity(registerForm, authenticationCode));
            rq.redirect("/register?code=" + authenticationCode + "&email=" + registerForm.getEmail());
        }
        
        
        return oAuth2User;
    }
    
    
    // 표준화 되지 않은 OAtuh2User를 리소스서버별로 특화된 ProviderUser 구현체로 변환
    private ProviderUser providerUser(OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        
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