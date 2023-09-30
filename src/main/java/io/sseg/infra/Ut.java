package io.sseg.infra;


import java.security.SecureRandom;
import java.util.Random;

public class Ut {
    
    
    public static int randomNumber() {
        Random random = new Random();
        return 111111 + random.nextInt(888889);  // 999999 - 111111 = 888888, 그리고 1을 더해서 범위를 888889로 조정
    }
    

    public static String randomString() {
        
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom RANDOM = new SecureRandom();
        
        StringBuilder result = new StringBuilder(10);
        
        for (int i = 0; i < 10; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            result.append(CHARACTERS.charAt(randomIndex));
        }
        
        return result.toString();
    }
}
