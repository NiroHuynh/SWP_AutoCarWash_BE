package com.swp.autocarwash.subscription.dto.request;

import com.swp.autocarwash.subscription.entity.enums.PlanType;
import com.swp.autocarwash.subscription.entity.enums.SubscriptionPlanStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

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

    @NotNull(message = "SERVICE_PACKAGE_REQUIRED")
    private Integer servicePackageId;

    @NotNull(message = "INVALID_PLAN_TYPE")
    private String planType;

    /**
     * Validate theo planType trong service
     */
    private Integer maxVehicleCount;

    @NotNull(message = "INVALID_STATUS")
    private String status;
}
