package io.sseg.base.http;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SsegApiResponseStatus {
    
    // Success code
    SUCCESS(1, "Request successfully processed")
    
    // error code
    // 기본 error code는 10부터 시작, 앞의 2자리는 큰 범주의 에러 타입을 나타내고, 그 뒤의 3자리는 세부 에러타입.
    , INTERNAL_SERVER_ERROR(-1, "Server internal error for unknown reason. please re-request later And if the error persists, please contact to " + "sechan100@gmail.com")
    , UNKNOWN_ERROR(0, "Request failed with unknown error")
    
    , INVALID_PARAMETER(10, "Invalid parameter")
        , NULL_PARAMETER(10100, "Required parameter is null")
        , INVALID_APP_ID(10101, "Invalid appId")
        , INVALID_APP_SECRET(10102, "Invalid appSecret")
        , INVALID_REFRESH_TOKEN(10103, "Invalid refresh token")
    
    
    , RESOURCE_NOT_FOUND(20, "Resource not found")
        , APPLICATION_NOT_FOUND(20100, "Application not found. Application finded by your appId is null.")
        , TEMPLATE_NOT_FOUND(20101, "Template not found. Template finded by your templateName is null.")
    
    , UNAUTHORIZED(30, "Unauthorized")
        , TOKEN_EXPIRED(30100, "Access token expired")
        , INVALID_ACCESS_TOKEN(30102, "Invalid access token")
        , INVALID_TOKEN_TYPE(30103, "Token type is invalid. You may have sent a different type of token.")
    
    , PROCESSING_ERROR(50, "Processing error")
        , EMAIL_SENDING_ERROR(50100, "Email sending error")
        , TEMPLATE_PARSING_ERROR(50101, "Fail to parse Email template with requeted template args.")
        , EMAIL_TEMPLATE_VARIABLE_NOT_FOUND(50102, "Email template variable not found")
    
    
    ;
    public final int statusCode;
    public final String detail;
    
    
}
