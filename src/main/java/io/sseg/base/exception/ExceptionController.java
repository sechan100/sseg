package io.sseg.base.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice()
public class ExceptionController {
    
    // springboot-starter-validation exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(fieldError -> fieldError.getCode() + ": " + fieldError.getDefaultMessage())
//                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        
        return ResponseEntity.badRequest().body(errors);
    }
    

}
