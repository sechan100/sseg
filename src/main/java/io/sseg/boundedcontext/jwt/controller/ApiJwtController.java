package io.sseg.boundedcontext.jwt.controller;


import io.sseg.base.aop.RequireJwtToken;
import io.sseg.base.http.ApiResponse;
import io.sseg.base.http.SsegApiResponseStatus;
import io.sseg.base.jwt.JwtProvider;
import io.sseg.base.constants.JwtProperties;
import io.sseg.base.jwt.JwtTokenType;
import io.sseg.base.request.Rq;
import io.sseg.boundedcontext.application.entity.Application;
import io.sseg.boundedcontext.application.service.ApplicationService;
import io.sseg.boundedcontext.jwt.model.JwtTokenDto;
import io.sseg.infra.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jwt")
public class ApiJwtController {
    
    private final Rq rq;
    private final JwtProvider jwtProvider;
    private final JwtProperties properties;
    private final ApplicationService applicationService;
    
    
    @GetMapping("/application/refreshToken")
    public ResponseEntity<ApiResponse<String>> getNewRefreshToken(@RequestParam String appId, @RequestParam String appSecret){
        
        // 파라미터 null
        if(appId == null || appSecret == null){
            return ApiResponse.badRequest(SsegApiResponseStatus.NULL_PARAMETER);
        }
        
        Application application = applicationService.findByAppId(appId);
        
        // 애플리케이션 엔티티가 존재하지 않음
        if(application == null){
            return ApiResponse.notFound(SsegApiResponseStatus.APPLICATION_NOT_FOUND);
        }
        
        // 애플리케이션의 appSecret 불일치
        if(!Ut.passwordEncoder.matches(appSecret, application.getAppSecret())){
            return ApiResponse.unauthorized(SsegApiResponseStatus.INVALID_APP_SECRET);
        }
        
        // refresh token 토큰 생성
        String refreshToken = jwtProvider.generateRefreshToken(appId);
        
        // DB에 refresh token 저장
        applicationService.saveRefreshToken(appId, refreshToken);
        
        return ApiResponse.success(refreshToken);
    }

    @GetMapping("/application/accessToken")
    @RequireJwtToken(JwtTokenType.REFRESH_TOKEN)
    public ResponseEntity<ApiResponse<String>> getNewAccessToken() {
        
        JwtTokenDto tokenDto = rq.getJwtToken();
        
        
        Application application = applicationService.findByAppId(tokenDto.getAppId());
        
        // appId를 통해 가져온 application 데이터 존재성 체크
        if(application == null){
            return ApiResponse.notFound(SsegApiResponseStatus.APPLICATION_NOT_FOUND);
        }
        
        // 가져온 application 엔티티의 refresh token과의 일치여부 체크
        if(!tokenDto.getToken().equals(application.getRefreshToken())){
            return ApiResponse.unauthorized(SsegApiResponseStatus.INVALID_APP_SECRET);
        }
        
        // access token 토큰 생성
        String accessToken = jwtProvider.generateAccessToken(tokenDto.getAppId());
        
        return ApiResponse.success(accessToken);
    }
    

}
