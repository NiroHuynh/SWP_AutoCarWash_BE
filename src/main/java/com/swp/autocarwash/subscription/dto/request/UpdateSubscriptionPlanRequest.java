package com.swp.autocarwash.subscription.dto.request;

import com.swp.autocarwash.subscription.entity.enums.PlanType;
import com.swp.autocarwash.subscription.entity.enums.SubscriptionPlanStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSubscriptionPlanRequest {

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

    /**
     * Validate theo planType trong service
     */
    private Integer maxVehicleCount;

    @NotNull(message = "INVALID_STATUS")
    private String status;

    // Các add-on tạo nên nội dung gói (thay cho servicePackageId trước đây) - nếu danh sách này
    // đổi khác so với add-on hiện có của servicePackage đang gắn, service sẽ tự tạo 1 Service
    // Package mới riêng (nếu package hiện tại đang dùng chung với gói khác) hoặc cập nhật tại chỗ
    // (nếu package đã là riêng của gói này). Bắt buộc chọn ít nhất 1.
    @NotNull(message = "ADDON_SERVICES_REQUIRED")
    private List<Integer> addonServiceIds;
}
