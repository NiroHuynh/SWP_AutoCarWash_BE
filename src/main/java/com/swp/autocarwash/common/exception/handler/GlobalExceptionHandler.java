package com.swp.autocarwash.common.exception.handler;

import com.swp.autocarwash.common.exception.BaseException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;


import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


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
