package com.swp.autocarwash.subscription.dto.request;

import com.swp.autocarwash.subscription.entity.enums.PlanType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSubscriptionPlanRequest {

    @NotBlank(message = "PLAN_NAME_REQUIRED")
    private String planName;

    @NotNull(message = "INVALID_PRICE")
    @DecimalMin(value = "0.01", message = "INVALID_PRICE")
    private BigDecimal price;

    @NotNull(message = "INVALID_DURATION_DAYS")
    @Positive(message = "INVALID_DURATION_DAYS")
    private Integer durationDays;

    private String description;

    @NotNull(message = "INVALID_PLAN_TYPE")
    private String planType;

    @NotNull(message = "INVALID_MAX_VEHICLE_COUNT")
    @Positive(message = "INVALID_MAX_VEHICLE_COUNT")
    private Integer maxVehicleCount;

    // Các add-on tạo nên nội dung gói (thay cho việc chọn 1 Service Package có sẵn) - hệ thống
    // sẽ tự tạo 1 Service Package mới riêng cho gói này từ danh sách add-on được chọn, để gói vẫn
    // có servicePackageId hợp lệ cho booking/walk-in tính quyền lợi. Bắt buộc chọn ít nhất 1.
    @NotNull(message = "ADDON_SERVICES_REQUIRED")
    private List<Integer> addonServiceIds;
}
