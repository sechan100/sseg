package io.sseg.base.http;

import io.sseg.base.properties.CustomProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
public enum SsegApiResponseStatus {
    
    // Success code
    SUCCESS(1, "Request successfully processed")
    
    // error code
    // 기본 error code는 100부터 시작, 앞의 3자리는 기본 에러 타입을 나타내고, 그 뒤의 자리는 세부 에러타입.
    , INTERNAL_SERVER_ERROR(-1, "Server internal error for unknown reason. please re-request later And if the error persists, please contact to " + "sechan100@gmail.com")
    , UNKNOWN_ERROR(0, "Request failed with unknown error")
    , INVALID_PARAMETER(100, "Invalid parameter")
        , NULL_PARAMETER(1001, "Required parameter is null")
        , INVALID_APP_ID(1002, "Invalid appId")
        , INVALID_APP_SECRET(1003, "Invalid appSecret")
    , RESOURCE_NOT_FOUND(200, "Resource not found")
        , APPLICATION_NOT_FOUND(1012, "Application not found")
    ;
    
    public final int statusCode;
    public final String msg;
    
    
}
