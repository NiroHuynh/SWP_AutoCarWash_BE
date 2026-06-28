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
    UNAUTHORIZED(
            HttpStatus.UNAUTHORIZED,
            "AUTH_001",
            "Unauthorized access"
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

    BOOKING_NOT_CHECKED_IN(
            HttpStatus.BAD_REQUEST,
            "BOOKING_006",
            "Booking chưa ở trạng thái check-in, không thể hủy theo luồng này"
    ),
    BOOKING_NOT_COMPLETED(
            HttpStatus.BAD_REQUEST,
            "BOOKING_007",
            "Booking chưa ở trạng thái COMPLETED, chưa thể thu tiền"
    ),
    VEHICLE_ALREADY_BOOKED(
            HttpStatus.BAD_REQUEST,
            "BOOKING_003",
            "Vehicle already has a booking on this date"
    ),
    BOOKING_SLOT_ALREADY_USED(
            HttpStatus.CONFLICT,
            "BOOKING_004",
            "Booking slot already used"
    ),
    BOOKING_INVOICE_ALREADY_EXISTS(
            HttpStatus.CONFLICT,
            "BOOKING_001",
            "Booking already has an invoice"
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
    INSUFFICIENT_PAYMENT_AMOUNT(
            HttpStatus.BAD_REQUEST,
            "PAYMENT_002",
            "Số tiền khách đưa không đủ để thanh toán"
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
    ),

    EARLY_ARRIVAL_SLOT_FULL(
            HttpStatus.BAD_REQUEST,
            "CHECK_IN_QUEUE_001",
            "Booking is too early. Please wait until your scheduled time or check available slots later"
    ),
    NO_ALLOCATED_TIME_SLOT(
            HttpStatus.BAD_REQUEST,
            "CHECK_IN_QUEUE_002",
            "Booking has no allocated time slot"
    ),
    VEHICLE_CHECKIN_RESTRICTED(
            HttpStatus.BAD_REQUEST,
            "CHECK_IN_QUEUE_003",
            "Vehicle is currently restricted due to past violations. Please collect 20,000 VND deposit at the counter before check-in."
    ),
    PENALTY_ONLY_FOR_WALK_IN(
            HttpStatus.BAD_REQUEST,
            "CHECK_IN_QUEUE_004",
            "Penalty deposit only applies to WALK_IN bookings"
    ),
    VEHICLE_CLEAR_NO_PENALTY(
            HttpStatus.BAD_REQUEST,
            "CHECK_IN_QUEUE_005",
            "This vehicle is not currently restricted - no penalty deposit required"
    ),
    SYSTEM_SETTING_NOT_FOUND(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "SYSTEM_SETTING_001",
            "System setting not found for key. Please ask Admin to configure"
    ),
    INVALID_CONFIG_VALUE_FORMAT(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "SYSTEM_SETTING_002",
            "System setting [%s] has invalid numeric value"
    ),
    SERVICE_PACKAGE_NOT_EXIST(
            HttpStatus.BAD_REQUEST,
            "SERVICE_PACKAGE_001",
            "This service package not exist in the system"

    ),
    SERVICE_PACKAGE_ADD_ON_NOT_EXIST(
            HttpStatus.BAD_REQUEST,
            "SERVICE_PACKAGE_002",
            "This service package add on not exist in the system"
    ),
    PENALTY_DEPOSIT_NOT_CONFIRMED(
            HttpStatus.BAD_REQUEST,
            "PENALTY_DEPOSIT_001",
            "The 20,000 VND penalty deposit has not been confirmed by the staff."
    ),
    SERVICE_SLOT_NOT_AVAILABLE(
            HttpStatus.BAD_REQUEST,
            "BOOKING_SLOT_001",
            "The selected service slot does not exist or is no longer available."
    ),
    VEHICLE_NOT_IN_VIOLATION_RESTRICTION(
            HttpStatus.BAD_REQUEST,
            "VEHICLE_001",
            "Vehicle not in violation restriction"
    ),
    VEHICLE_NOT_BELONG_TO_CUSTOMER(
            HttpStatus.BAD_REQUEST,
            "VEHICLE_002",
            "Vehicle not belong to the customer"
    ),
    VEHICLE_ALREADY_BOOKED_THIS_SLOT(
            HttpStatus.BAD_REQUEST,
            "VEHICLE_003",
            "Vehicle already booked this slot today"
    ),
    INVALID_SLOT_QUANTITY(
            HttpStatus.BAD_REQUEST,
            "BOOKING_SLOT_001",
            "Invalid slot required for this service package"
    ),
    SLOTS_MUST_BE_CONSECUTIVE(
            HttpStatus.BAD_REQUEST,
            "BOOKING_SLOT_002",
            "Slots must be consecutive"
    ),
    QUEUE_TICKET_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "QUEUE_001",
            "Queue ticket not found"
    ),
    QUEUE_TICKET_NOT_WAITING(
            HttpStatus.BAD_REQUEST,
            "QUEUE_002",
            "Queue ticket is not in WAITING status"
    ),
    QUEUE_TICKET_NOT_IN_SERVICE(
            HttpStatus.BAD_REQUEST,
            "QUEUE_003",
            "Queue ticket is not in IN_SERVICE status"
    ),
    WASH_LANE_NONE_AVAILABLE(
            HttpStatus.BAD_REQUEST,
            "STATION_002",
            "Không còn làn rửa trống"
    ),
    BOOKING_NOT_WASHING(
            HttpStatus.BAD_REQUEST,
            "BOOKING_007",
            "Booking chưa ở trạng thái WASHING"
    );








    private final HttpStatus status;
    private final String code;
    private final String message;



}
