package com.swp.autocarwash.customer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Chức năng: Overlay chi tiết khách hàng cho Admin (FE-US-09-03 AC1/AC2).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDetailResponse {

    private Long customerId;

    private String customerCode;

    private String fullName;

    private String email;

    private String phone;

    private String tier;

    private Integer totalPoints;

    private LocalDateTime lastVisit;

    /** "ACTIVE" hoặc "INACTIVE" — INACTIVE nếu tài khoản bị khoá hoặc có >=3 booking NO_SHOW. */
    private String accountStatus;

    private List<VehicleInfo> vehicles;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VehicleInfo {

        private String brandName;

        private String color;

        private String licensePlate;

        /** "UNLIMITED" | "FAMILY" | null nếu xe không thuộc gói subscription nào đang active. */
        private String activeSubscriptionType;
    }
}
