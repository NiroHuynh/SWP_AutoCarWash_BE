package com.swp.autocarwash.common.exception.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.nimbusds.jose.JOSEException;
import com.swp.autocarwash.auth.exception.AccountDisabledException;
import com.swp.autocarwash.common.exception.BaseException;
import com.swp.autocarwash.common.exception.CustomerRestrictedException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.subscription.entity.enums.PlanType;
import jakarta.servlet.http.HttpServletRequest;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

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
        errorBody.put("message", "You do not have permission to perform this action");
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

    //Hứng lỗi: customer đang bị hạn chế booking do vi phạm -> kèm số ngày còn lại để FE hiển thị
    @ExceptionHandler(CustomerRestrictedException.class)
    public ResponseEntity<Map<String, Object>> handleCustomerRestricted(CustomerRestrictedException ex){
        var errorCode = ex.getErrorCode();
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("success", "false");
        errorBody.put("message", errorCode.getMessage());
        errorBody.put("errorCode", errorCode.getCode());
        errorBody.put("remainingDays", ex.getRemainingDays());
        return new ResponseEntity<>(errorBody, errorCode.getStatus());
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
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidation(
            MethodArgumentNotValidException ex) {

        String errorKey = ex.getBindingResult()
                .getFieldError()
                .getDefaultMessage();

        ErrorCode errorCode = ErrorCode.valueOf(errorKey);

        return ResponseEntity.status(400)
                .body(
                        ApiResponse.error(
                                errorCode.getMessage(),
                                errorCode.getCode(),
                                null
                        )
                );
    }

//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity<ApiResponse<Object>> handleHttpMessageNotReadable(
//            HttpMessageNotReadableException ex
//    ) {
//
//        Throwable cause = ex.getCause();
//
//        if (cause instanceof InvalidFormatException invalidFormatException
//                && invalidFormatException.getTargetType().equals(PlanType.class)) {
//
//            ErrorCode errorCode = ErrorCode.INVALID_PLAN_TYPE;
//
//            return ResponseEntity
//                    .status(errorCode.getStatus())
//                    .body(
//                            ApiResponse.error(
//                                    errorCode.getMessage(),
//                                    errorCode.getCode(),
//                                    null
//                            )
//                    );
//        }
//
//        return ResponseEntity
//                .badRequest()
//                .body(
//                        ApiResponse.error(
//                                "Invalid request body.",
//                                "INVALID_REQUEST",
//                                null
//                        )
//                );
//    }
}
