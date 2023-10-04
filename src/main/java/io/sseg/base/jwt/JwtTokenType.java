package io.sseg.base.jwt;

public enum JwtTokenType {
    
    
    ACCESS_TOKEN("ACCESS_TOKEN"), REFRESH_TOKEN("REFRESH_TOKEN");
    
    public static final String CLAIM_KEY = "tokenType";
    public final String str;
    
    JwtTokenType(String str){
        this.str = str;
    }
    
    public static JwtTokenType getJwtTokenTypeByString(String str){
        if(str.equals(ACCESS_TOKEN.str)){
            return ACCESS_TOKEN;
        }else if(str.equals(REFRESH_TOKEN.str)){
            return REFRESH_TOKEN;
        }
        throw new IllegalArgumentException("Invalid token type");
    }
}
