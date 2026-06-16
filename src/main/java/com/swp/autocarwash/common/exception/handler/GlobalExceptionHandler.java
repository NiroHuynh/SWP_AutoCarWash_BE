package com.swp.autocarwash.common.exception.handler;

import com.swp.autocarwash.common.exception.BaseException;
import com.swp.autocarwash.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestControllerAdvice
public class GlobalExceptionHandler {

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
