package io.sseg.base.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;


@Component
@RequiredArgsConstructor
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String SECRET_KEY;
    
    @Value("${jwt.expireTimeMinutes}")
    private Integer expireTimeMinutes;
    
    public String generateToken(Long appId) {
        return Jwts.builder()
                .setSubject(appId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * expireTimeMinutes))
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
        byte[] decodedKey = SECRET_KEY.getBytes();
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
    }
    
    
    public Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }
}
