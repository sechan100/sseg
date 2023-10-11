package io.sseg.base.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.sseg.base.http.ApiResponse;
import io.sseg.base.http.SsegApiResponseStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice()
public class ExceptionController {
    
    // springboot-starter-validation의 @Valid 유효성 검사 실패 예외
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> "'" + fieldError.getField() + "' 변수가 다음 제약조건을 만족시키지 못합니다: " + fieldError.getCode())
                .collect(Collectors.toList());
        
        return ResponseEntity.badRequest().body(errors);
    }
    

}
