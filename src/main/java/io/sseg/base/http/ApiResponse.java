package io.sseg.base.http;


import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

@Data
@Builder
public class ApiResponse<T> {
    
    private T data;
    
    SsegApiResponseStatus status;
    
    private String msg;
    
    public ApiResponse(T data, SsegApiResponseStatus status, String msg){
        this.data = data;
        this.status = status;
        
        this.msg = Objects.requireNonNullElse(msg, "provided additional message is null.");
    }
    
    // 200 ok
    public static <T> ResponseEntity<ApiResponse<T>> ok(T data){
        return ResponseEntity.ok(new ApiResponse<>(data, SsegApiResponseStatus.SUCCESS, null));
    }
    
    // custom error
    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus httpStatus, SsegApiResponseStatus status, String msg){
        return ResponseEntity.status(httpStatus).body(new ApiResponse<>(null, status, msg));
    }
    
    // 400 bad request
    public static <T> ResponseEntity<ApiResponse<T>> badRequest(SsegApiResponseStatus status, String msg){
        return ResponseEntity.badRequest().body(new ApiResponse<>(null, status, msg));
    }
    
    
    // 401 unauthorized
    public static <T> ResponseEntity<ApiResponse<T>> unauthorized(SsegApiResponseStatus status, String msg){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(null, status, msg));
    }
    
    // 403 forbidden
    public static <T> ResponseEntity<ApiResponse<T>> forbidden(SsegApiResponseStatus status, String msg){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(null, status, msg));
    }
    
    // 404 not found
    public static <T> ResponseEntity<ApiResponse<T>> notFound(SsegApiResponseStatus status, String msg){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(null, status, msg));
    }
    
    // 409 Conflict
    public static <T> ResponseEntity<ApiResponse<T>> conflict(SsegApiResponseStatus status, String msg){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse<>(null, status, msg));
    }
    
    // 422 Unprocessable Entity
    public static <T> ResponseEntity<ApiResponse<T>> unprocessableEntity(SsegApiResponseStatus status, String msg){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ApiResponse<>(null, status, msg));
    }
    
    
    // 400 bad request
    public static <T> ResponseEntity<ApiResponse<T>> badRequest(SsegApiResponseStatus status){
        return ResponseEntity.badRequest().body(new ApiResponse<>(null, status, null));
    }
    
    
    // 401 unauthorized
    public static <T> ResponseEntity<ApiResponse<T>> unauthorized(SsegApiResponseStatus status){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(null, status, null));
    }
    
    // 403 forbidden
    public static <T> ResponseEntity<ApiResponse<T>> forbidden(SsegApiResponseStatus status){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(null, status, null));
    }
    
    // 404 not found
    public static <T> ResponseEntity<ApiResponse<T>> notFound(SsegApiResponseStatus status){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(null, status, null));
    }
    
    // 409 Conflict
    public static <T> ResponseEntity<ApiResponse<T>> conflict(SsegApiResponseStatus status){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse<>(null, status, null));
    }
    
    // 422 Unprocessable Entity
    public static <T> ResponseEntity<ApiResponse<T>> unprocessableEntity(SsegApiResponseStatus status){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ApiResponse<>(null, status, null));
    }
}
