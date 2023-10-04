package io.sseg.base.aop;


import io.sseg.base.jwt.JwtTokenType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireJwtToken {
    JwtTokenType value() default JwtTokenType.ACCESS_TOKEN;
}
