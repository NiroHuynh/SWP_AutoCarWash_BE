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
    CUSTOMER_NOT_ELIGIBLE_FOR_BOOKING(
            HttpStatus.BAD_REQUEST,
        "CUSTOMER_004",
                "Customer is not eligible for booking"
    ),
    CUSTOMER_RESTRICTED(
            HttpStatus.BAD_REQUEST,
        "CUSTOMER_005",
                "Customer account is restricted"
    ),
    BOOKING_PRICE_CALCULATION_FAILED(
            HttpStatus.BAD_REQUEST,
            "BOOKING_004",
            "Failed to calculate booking price"
    ),
    VOUCHER_INVALID(
            HttpStatus.BAD_REQUEST,
            "VOUCHER_001",
            "Voucher is invalid or expired"
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
    ROLE_NOT_FOUND(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "AUTH_004",
            "Role not found"
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
    ),
    TIER_NOT_FOUND(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "LOYALTY_001",
            "Customer tier not found"
    ),
    CUSTOMER_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "CUSTOMER_001",
            "Customer not found"
    ),;
    BOOKING_INVALID_SLOT(
            HttpStatus.BAD_REQUEST,
            "BOOKING_004",
            "Invalid slot selection"
    ),
    BOOKING_SLOT_NOT_AVAILABLE(
            HttpStatus.BAD_REQUEST,
            "BOOKING_005",
            "Slot is not available"
    ),
    VEHICLE_NOT_FOUND(
            HttpStatus.NOT_FOUND,
    "VEHICLE_001",
            "Vehicle not found"
    ),
    VEHICLE_NOT_OWNED(
            HttpStatus.FORBIDDEN,
        "VEHICLE_002",
                "Vehicle does not belong to customer"
    ),

    VEHICLE_INACTIVE(
            HttpStatus.BAD_REQUEST,
        "VEHICLE_003",
                "Vehicle is inactive or deleted"
    ),
    VOUCHER_NOT_FOUND(
            HttpStatus.NOT_FOUND,
        "VOUCHER_002",
                "Voucher not found"
    ),
    VOUCHER_EXPIRED(
            HttpStatus.BAD_REQUEST,
        "VOUCHER_003",
                "Voucher has expired"
    ),
    VOUCHER_USAGE_LIMIT_REACHED(
            HttpStatus.BAD_REQUEST,
        "VOUCHER_004",
                "Voucher usage limit reached"
    ),
    VOUCHER_NOT_APPLICABLE(
            HttpStatus.BAD_REQUEST,
        "VOUCHER_005",
                "Voucher not applicable for this order"
    ),
    ADDON_SERVICE_NOT_FOUND(
            HttpStatus.NOT_FOUND,
        "SERVICE_001",
                "Addon service not found"
    ),
    ADDON_SERVICE_INVALID(
            HttpStatus.BAD_REQUEST,
        "SERVICE_002",
                "Invalid addon service request"
    ),
    INVALID_SERVICE_PACKAGE_ID(
            HttpStatus.BAD_REQUEST,
            "SERVICE_PACKAGE_001",
            "Service package id is invalid"
    ),
    SERVICE_PACKAGE_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "SERVICE_PACKAGE_002",
            "Service package not found"
    );

    private final HttpStatus status;
    private final String code;
    private final String message;
}
