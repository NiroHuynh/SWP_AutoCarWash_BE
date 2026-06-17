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
    PROVINCE_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "LOCATION_001",
            "Province not found"
    ),
    STATION_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "STATION_001",
            "Station not found"
    ),
    BOOKING_WINDOW_NOT_AVAILABLE(
            HttpStatus.BAD_REQUEST,
            "BOOKING_003",
            "Booking window not available"
    ),
    NO_AVAILABLE_SLOTS_FOR_DATE(
            HttpStatus.BAD_REQUEST,
            "NO_AVAILABLE_SLOTS_FOR_DATE",
            "Không còn khung giờ trống trong ngày đã chọn. Vui lòng đổi ngày, đổi trạm hoặc đổi gói dịch vụ khác."
    ),
    SLOT_CAPACITY_EXCEEDED(
            HttpStatus.BAD_REQUEST,
            "BOOKING_SLOT_002",
            "Slot capacity exceeded"
    ),
    NO_VEHICLE_REGISTERED(
            HttpStatus.BAD_REQUEST,
            "NO_VEHICLE_REGISTERED",
            "Bạn chưa có phương tiện nào. Vui lòng thêm xe trước khi đặt lịch."
    ),
    CUSTOMER_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "CUSTOMER_002",
            "Customer not found"
    ),
    CUSTOMER_TIER_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "CUSTOMER_003",
            "Customer tier not found"
    ),
    PAYMENT_FAILED(
            HttpStatus.BAD_REQUEST,
            "PAYMENT_001",
            "Payment failed"
    );

    private final HttpStatus status;
    private final String code;
    private final String message;
}
