package io.sseg.boundedcontext.email.constants;

public class EmailEnums {
    
    public enum Status {
        SUCCESS,         // 이메일 전송 성공
        FAIL,            // 이메일 전송 실패 (일반적인 실패 상황)
        PENDING,         // 이메일 전송 대기 중 (예: 큐에 저장된 상태)
        RETRY,           // 이메일 전송 재시도 중
        DELIVERED,       // 이메일이 수신자에게 성공적으로 전달됨
        UNDELIVERED      // 이메일이 수신자에게 전달되지 못함
    }
    
    public enum ErrorCode {
        NO_ERROR,                // 오류 없음
        INVALID_RECIPIENT,       // 잘못된 수신자 주소
        SMTP_ERROR,              // SMTP 서버 오류
        TIMEOUT,                 // 이메일 전송 시간 초과
        MESSAGE_TOO_LARGE,       // 이메일 메시지 크기 초과
        AUTHENTICATION_FAILED,   // SMTP 서버 인증 실패
        RATE_LIMITED,            // 전송 속도 제한 초과
        UNKNOWN_ERROR            // 알 수 없는 오류
    }
}

