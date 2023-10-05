package io.sseg.base.aop;

import io.sseg.base.request.Rq;
import io.sseg.boundedcontext.application.entity.Application;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.reflect.Parameter;

@Component
@RequiredArgsConstructor
@Aspect
public class AccessAllowedAspect {
    
    private final Rq rq;
    
    @Pointcut("execution(* io.sseg.boundedcontext.application.controller.*.*(..))")
    public void allControllerMethodOfApplicationDomain() {}
    
    
    
    @Before("allControllerMethodOfApplicationDomain()")
    public void ownershipCheck(JoinPoint joinPoint) {
        
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Parameter[] parameters = methodSignature.getMethod().getParameters();
        
        for (Parameter parameter : parameters) {
            
            PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
            
            if (pathVariable != null && "appId".equals(parameter.getName())) {
                
                Object[] args = joinPoint.getArgs();
                
                for (int i = 0; i < parameters.length; i++) {
                    String appId = (String) args[i];
                    rq.isAccessAllowed(appId, "appId", Application.class);
                }
                
            }
        }
    }
    
}
