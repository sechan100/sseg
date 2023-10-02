package io.sseg.boundedcontext.user.exception;

public class RegistrationException extends RuntimeException {

    public static final String USERNAME_DUPLICATION = "이미 사용중인 아이디입니다.";
    public static final String EMAIL_DUPLICATION = "이미 사용중인 이메일입니다.";
    public static final String INVALID_EMAIL = "유효하지 않은 이메일입니다.";
    public static final String INVALID_USERNAME = "유효하지 않은 아이디입니다.";
    public static final String INVALID_PASSWORD = "유효하지 않은 비밀번호입니다.";
    public static final String INVALID_PASSWORD_CONFIRM = "비밀번호가 일치하지 않습니다.";
    public static final String INVALID_PROVIDER = "유효하지 않은 소셜 로그인입니다.";
    
    
    public RegistrationException(String message) {
        super(message);
    }
}
