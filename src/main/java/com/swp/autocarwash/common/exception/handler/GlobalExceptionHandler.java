package com.swp.autocarwash.common.exception.handler;

import com.swp.autocarwash.auth.exception.AccountDisabledException;
import com.swp.autocarwash.common.exception.BaseException;
import com.swp.autocarwash.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    //Hứng lỗi: sai tài khoản hoặc sai mật khẩu -> 401
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials(BadCredentialsException ex){
        Map<String,String> errorBody = new HashMap<>();
        errorBody.put("errorCode", "AUTH_001");
        errorBody.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorBody);
    }

    //Hứng lỗi: tài khoản bị vô hiệu hoá -> 403
    @ExceptionHandler(AccountDisabledException.class)
    public ResponseEntity<Map<String,String>> handleAccountDisabled(AccountDisabledException ex){
        Map<String,String> errorBody = new HashMap<>();
        errorBody.put("errorCode","AUTH_002");
        errorBody.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorBody);
    }

    //Hứng lỗi: không lường trước được
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRunTimeException(RuntimeException ex){
        Map<String,String> errorBody = new HashMap<>();
        errorBody.put("errorCode", "SYS_500");
        errorBody.put("message",ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody);
    }



}
