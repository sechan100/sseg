package io.sseg.base.aop;

import io.sseg.base.exception.ApiCallFailureException;
import io.sseg.base.http.ApiResponse;
import io.sseg.base.http.SsegApiResponseStatus;
import io.sseg.base.jwt.JwtTokenType;
import io.sseg.base.request.Rq;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Aspect
public class JwtTokenTypeValidateAspect {
    
    private final Rq rq;
    
    @Pointcut("@annotation(io.sseg.base.aop.RequireJwtToken)")
    public void requireJwtTokenMethods() {}
    
    
    
    @Before("requireJwtTokenMethods()")
    public void beforeAdvice(JoinPoint joinPoint) {
        RequireJwtToken requireJwtTokenAnnot = ((MethodSignature)joinPoint.getSignature()).getMethod().getAnnotation(RequireJwtToken.class);
        
        if(requireJwtTokenAnnot == null){
            throw new IllegalArgumentException("RequireJwtToken annotation not found");
        } else {
            JwtTokenType requiredType = requireJwtTokenAnnot.value();
            JwtTokenType requestedType = rq.getJwtToken().getType();
            if(requiredType != requestedType){
                throw new ApiCallFailureException(ApiResponse.badRequest(SsegApiResponseStatus.INVALID_TOKEN_TYPE));
            }
        }
    }
    
}
