package io.sseg.boundedcontext.jwt.model;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.sseg.base.jwt.JwtTokenType;
import lombok.Data;

import java.security.Key;

@Data
public class JwtTokenDto {
    
    private JwtTokenType type;
    private String token;
    private String appId;
    
    
    
    public boolean isAccessToken(){
        return type == JwtTokenType.ACCESS_TOKEN;
    }
    
    public boolean isRefreshToken(){
        return type == JwtTokenType.REFRESH_TOKEN;
    }
    
    public JwtTokenDto(String token, Key signKey){
        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(signKey).build().parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        String appId = claims.getSubject();
        
        this.token = token;
        this.appId = appId;
        
        
        String tokenTypeStr = claims.get(JwtTokenType.CLAIM_KEY, String.class);
        JwtTokenType tokenType = JwtTokenType.getJwtTokenTypeByString(tokenTypeStr);
        
        switch(tokenType){
            case ACCESS_TOKEN:
                this.type = JwtTokenType.ACCESS_TOKEN;
                break;
            case REFRESH_TOKEN:
                this.type = JwtTokenType.REFRESH_TOKEN;
                break;
            default :
                throw new IllegalArgumentException("Invalid token type");
        }
    }
}

