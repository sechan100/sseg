package io.sseg.base.exception;

import io.sseg.base.http.ApiResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@Getter
@RequiredArgsConstructor
public class ApiCallFailureException extends RuntimeException {
    
    private final ResponseEntity<ApiResponse<Object>> response;
    
}
