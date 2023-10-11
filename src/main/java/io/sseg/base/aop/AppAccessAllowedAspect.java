package io.sseg.base.aop;

import io.sseg.base.request.Rq;
import io.sseg.boundedcontext.application.entity.Application;
import io.sseg.boundedcontext.application.service.ApplicationService;
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
public class AppAccessAllowedAspect {
    
    private final Rq rq;
    private final ApplicationService applicationService;
    
    @Pointcut("execution(* io.sseg.boundedcontext.application.controller.*.*(..))")
    public void allControllerMethodOfApplicationDomain() {}
    
    
    
    @Before("allControllerMethodOfApplicationDomain()")
    public void ownershipCheck(JoinPoint joinPoint) {
        
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Parameter[] parameters = methodSignature.getMethod().getParameters();
        
        for (Parameter parameter : parameters) {
            
            PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
            
            if (pathVariable != null) {
                
                Object[] args = joinPoint.getArgs();
            
                String appId = (String) args[0];
                Long applciationId = applicationService.findIdByAppId(appId);
                rq.isAccessAllowed(applciationId, Application.class);
                
            }
        }
    }
    
}
