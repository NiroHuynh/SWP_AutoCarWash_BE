package com.swp.autocarwash.common.exception.handler;

import com.nimbusds.jose.JOSEException;
import com.swp.autocarwash.auth.exception.AccountDisabledException;
import com.swp.autocarwash.common.exception.BaseException;
import com.swp.autocarwash.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
//Xử lí ngoại lệ tập trung cho @RestController// bao bọc xung quanh @RestController


public class GlobalExceptionHandler {

    //Hứng lỗi: sai tài khoản hoặc sai mật khẩu -> 401
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials(BadCredentialsException ex){
        Map<String,String> errorBody = new HashMap<>();
        errorBody.put("success", "false");
        errorBody.put("message", ex.getMessage());
        errorBody.put("errorCode", "AUTH_001");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorBody);
    }

    //Hứng lỗi: tài khoản bị vô hiệu hoá -> 403
    @ExceptionHandler(AccountDisabledException.class)
    public ResponseEntity<Map<String,String>> handleAccountDisabled(AccountDisabledException ex){
        Map<String,String> errorBody = new HashMap<>();
        errorBody.put("success", "false");
        errorBody.put("message", ex.getMessage());
        errorBody.put("errorCode","AUTH_002");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorBody);
    }

    //Hứng lỗi: user đã login nhưng không đủ quyền (thiếu authority/role yêu cầu bởi @PreAuthorize) -> 403
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String,String>> handleAccessDenied(AccessDeniedException ex){
        Map<String,String> errorBody = new HashMap<>();
        errorBody.put("success", "false");
        errorBody.put("message", "Bạn không có quyền thực hiện hành động này");
        errorBody.put("errorCode", "AUTH_003");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorBody);
    }

    //Hứng lỗi: không lường trước được
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRunTimeException(RuntimeException ex){
        Map<String,String> errorBody = new HashMap<>();
        errorBody.put("success", "false");
        errorBody.put("message",ex.getMessage());
        errorBody.put("errorCode", "SYS_500");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody);
    }

    @ExceptionHandler(JOSEException.class)
    public ResponseEntity<Map<String, Object>> handleJOSEException(JOSEException ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("success", "false");
        errors.put("message", "Can not generate token now. Please try again!");
        errors.put("errorCode", "TOKEN_001");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Object>> handleBaseException(
            BaseException ex,
            HttpServletRequest request
    ){
        var error = ex.getErrorCode();
        return ResponseEntity
                .status(error.getStatus())
                .body(
                        ApiResponse.error(
                                error.getMessage(),
                                error.getCode(),
                                null
                        )
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(
            Exception ex
    ){
        return ResponseEntity
                .internalServerError()
                .body(
                        ApiResponse.error(
                                "Unexpected error",
                                "SYSTEM_ERROR",
                                null
                        )
                );
    }
}
