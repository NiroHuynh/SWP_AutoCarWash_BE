package com.swp.autocarwash.payment.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Chức năng: Chi tiết hóa đơn sau checkout cho staff xem (FE-63-US-01 AC02).
 */
@Data
@Builder
public class InvoiceDetailResponse {

    private Long invoiceId;
    private String invoiceStatus;
    private LocalDateTime paidAt;

    private Long bookingId;
    private String vehicleBrand;
    private String vehicleLicensePlate;
    private String servicePackageName;
    private LocalDate appointmentDate;
    private LocalDateTime checkInAt;
    private LocalDateTime checkOutAt;

    private List<ServiceLine> services;

    private BigDecimal rawAmount;
    private BigDecimal serviceAmount;
    private BigDecimal addonAmount;
    private BigDecimal voucherDiscount;
    private BigDecimal pointDiscount;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;

    private String paymentMethod;

    @Data
    @Builder
    public static class ServiceLine {
        private String name;
        private BigDecimal price;
    }
}
