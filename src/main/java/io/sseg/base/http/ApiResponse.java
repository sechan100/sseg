package io.sseg.base.http;


import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
public class ApiResponse<T> {
    
    private T data;
    
    private int status;
    
    private String msg;
    
    public ApiResponse(T data, SsegApiResponseStatus status){
        this.data = data;
        this.status = status.statusCode;
        this.msg = status.msg;
    }
    
    // 200 ok
    public static <T> ResponseEntity<ApiResponse<T>> success(T data){
        return ResponseEntity.ok(new ApiResponse<>(data, SsegApiResponseStatus.SUCCESS));
    }
    
    // custom error
    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus httpStatus, SsegApiResponseStatus status){
        return ResponseEntity.status(httpStatus).body(new ApiResponse<>(null, status));
    }
    
    // 400 bad request
    public static <T> ResponseEntity<ApiResponse<T>> badRequest(SsegApiResponseStatus status){
        return ResponseEntity.badRequest().body(new ApiResponse<>(null, status));
    }
    
    // 401 unauthorized
    public static <T> ResponseEntity<ApiResponse<T>> unauthorized(SsegApiResponseStatus status){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(null, status));
    }
    
    // 403 forbidden
    public static <T> ResponseEntity<ApiResponse<T>> forbidden(SsegApiResponseStatus status){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(null, status));
    }
    
    // 404 not found
    public static <T> ResponseEntity<ApiResponse<T>> notFound(SsegApiResponseStatus status){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(null, status));
    }
    
    // 409 Conflict
    public static <T> ResponseEntity<ApiResponse<T>> conflict(SsegApiResponseStatus status){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse<>(null, status));
    }
    
    // 422 Unprocessable Entity
    public static <T> ResponseEntity<ApiResponse<T>> unprocessableEntity(SsegApiResponseStatus status){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ApiResponse<>(null, status));
    }
    
    
    
    
}
