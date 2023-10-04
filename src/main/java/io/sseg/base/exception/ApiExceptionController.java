package io.sseg.base.exception;

import io.sseg.base.http.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionController {
    
    @ExceptionHandler(ApiCallFailureException.class)
    public ResponseEntity<ApiResponse<Object>> handleApiCallFailureException(ApiCallFailureException ex) {
        return ex.getResponse();
    }
}
