package com.swp.autocarwash.common.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INTERNAL_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "COMMON_001",
            "Internal server error"
    ),
    INVALID_REQUEST(
            HttpStatus.BAD_REQUEST,
            "COMMON_002",
            "Invalid request"
    ),
    USER_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "AUTH_001",
            "User not found"
    ),
    INVALID_TOKEN(
            HttpStatus.UNAUTHORIZED,
            "AUTH_002",
            "Invalid token"
    ),
    BOOKING_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "BOOKING_001",
            "Booking not found"
    ),
    BOOKING_CANCELLED(
            HttpStatus.BAD_REQUEST,
            "BOOKING_002",
            "Booking already cancelled"
    ),
    PAYMENT_FAILED(
            HttpStatus.BAD_REQUEST,
            "PAYMENT_001",
            "Payment failed"
    ),
    VALIDATION_FAILED(
            HttpStatus.BAD_REQUEST,
            "COMMON_003",
                    "Validation failed"
    ),
    EMAIL_ALREADY_EXISTS(
            HttpStatus.BAD_REQUEST,
            "AUTH_001",
                    "Email already exists"
    ),
    PHONE_ALREADY_EXISTS(
            HttpStatus.BAD_REQUEST,
            "AUTH_002",
                    "Phone already exists"
    ),
    INVALID_PASSWORD(
            HttpStatus.BAD_REQUEST,
            "AUTH_003",
                    "Password is invalid"
    ),
    LICENSE_PLATE_ALREADY_EXISTS(
            HttpStatus.BAD_REQUEST,
        "VEHICLE_001",
                "Biển số xe đã tồn tại trong hệ thống"
    ),
    VEHICLE_NOT_FOUND(
            HttpStatus.NOT_FOUND,
        "VEHICLE_002",
                "Vehicle not found"
    ),;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
