package io.sseg.base.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;


@Component
public class JwtUtil {
    
    private final String SECRET_KEY = "YOUR_SECRET_KEYdKJDKLFEIBIGO23478tfdfdhf8adDhfdfdsaghbdnfldknKNDXFds";
    private final Integer expireTimeMinutes = 30;
    
    public String generateToken(Long appId) {
        return Jwts.builder()
                .setSubject(appId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * expireTimeMinutes))
                .signWith(getSignKey())
                .compact();
    }
    
    public String extractAppId(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }
    
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    private Key getSignKey() {
        byte[] decodedKey = SECRET_KEY.getBytes();
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
    }
}
