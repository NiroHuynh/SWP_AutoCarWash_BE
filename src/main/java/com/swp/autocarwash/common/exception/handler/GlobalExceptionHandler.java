package com.swp.autocarwash.common.exception.handler;

import com.nimbusds.jose.JOSEException;
import com.swp.autocarwash.auth.exception.AccountDisabledException;
import com.swp.autocarwash.common.exception.BaseException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
    public ResponseEntity<?> handleBaseException(BaseException ex){
        //base exception nên chứa errorCode bên trong
        var errorCode = ex.getErrorCode();

        //Đóng gói JSON lỗi -> trả về cho Front end
        Map<String, Object> errorBody = Map.of(
                "success", "false",
                "message", errorCode.getMessage(),
                "errorCode", errorCode.getCode()
        );
        return new ResponseEntity<>(errorBody, errorCode.getStatus());
    }

//    @ExceptionHandler(BaseException.class)
//    public ResponseEntity<ApiResponse<Object>> handleBaseException(
//            BaseException ex,
//            HttpServletRequest request
//    ){
//        var error = ex.getErrorCode();
//        return ResponseEntity
//                .status(error.getStatus())
//                .body(
//                        ApiResponse.error(
//                                error.getMessage(),
//                                error.getCode(),
//                                null
//                        )
//                );
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(
            Exception ex
    ){
        System.out.println(ex.getMessage());
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

    /**
     * Handle DTO validation
     *
     * @Valid validation failed
     */
    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    public ResponseEntity<ApiResponse<Object>> handleValidation(
            MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors =
                new HashMap<>();


        ex.getBindingResult()
                .getFieldErrors()
                .forEach(
                        error ->
                                errors.put(
                                        error.getField(),
                                        error.getDefaultMessage()
                                )
                );

        return ResponseEntity
                .badRequest()
                .body(
                        ApiResponse.error(
                                "Validation failed",
                                ErrorCode.VALIDATION_FAILED.getCode(),
                                errors
                        )

                );
    }
}
