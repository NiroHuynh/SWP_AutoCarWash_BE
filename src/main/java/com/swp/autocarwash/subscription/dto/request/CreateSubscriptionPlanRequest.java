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

    @NotNull(message = "SERVICE_PACKAGE_REQUIRED")
    private Integer servicePackageId;

    @NotNull(message = "INVALID_PLAN_TYPE")
    private String planType;

    @NotNull(message = "INVALID_MAX_VEHICLE_COUNT")
    @Positive(message = "INVALID_MAX_VEHICLE_COUNT")
    private Integer maxVehicleCount;

    // Add-on đi kèm gói (marketing/quyền lợi hiển thị cho khách) - KHÔNG liên quan servicePackageId
    // ở trên (servicePackageId vẫn quyết định hạng dịch vụ rửa xe cho booking/walk-in).
    // Tuỳ chọn - có thể để trống/null nếu gói không kèm add-on nào.
    private List<Integer> addonServiceIds;
}
