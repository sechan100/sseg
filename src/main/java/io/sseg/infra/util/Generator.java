package io.sseg.infra.util;

import java.security.SecureRandom;
import java.util.Base64;

public class Generator {
    
    private SecureRandom secureRandom = new SecureRandom();
    
    
    public int generateRandomNumber() {
        java.util.Random random = new java.util.Random();
        return 111111 + random.nextInt(888889);  // 999999 - 111111 = 888888, 그리고 1을 더해서 범위를 888889로 조정
    }
    
    
    public String generateRandomString() {
        
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_.!~*";
        
        SecureRandom RANDOM = new SecureRandom();
        int randomStringLength = 25;
        
        StringBuilder result = new StringBuilder(randomStringLength);
        
        for (int i = 0; i < randomStringLength; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            result.append(CHARACTERS.charAt(randomIndex));
        }
        
        return result.toString();
    }
    
    public String generateUUID(){
        return java.util.UUID.randomUUID().toString();
    }
}
