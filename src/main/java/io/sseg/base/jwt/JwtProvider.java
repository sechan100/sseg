package io.sseg.base.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.sseg.base.constants.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class JwtProvider {
    
    private final JwtProperties properties;
    
    public String generateAccessToken(String appId) {
        return Jwts.builder()
                .setClaims(Map.of(JwtTokenType.CLAIM_KEY, JwtTokenType.ACCESS_TOKEN))
                .setSubject(appId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * properties.getExpirationPeriodMinutes()))
                .signWith(getSignKey())
                .compact();
    }
    
    public String generateRefreshToken(String appId) {
        return Jwts.builder()
                .setClaims(Map.of(JwtTokenType.CLAIM_KEY, JwtTokenType.REFRESH_TOKEN))
                .setSubject(appId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * properties.getExpirationPeriodDay()))
                .signWith(getSignKey())
                .compact();
    }
    
    
    public String extractAppId(String token) {
        return getClaims(token).getSubject();
    }
    
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public Key getSignKey() {
        byte[] decodedKey = properties.getSecretKey().getBytes();
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
    }
    
    
    public Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }
}
