package com.swp.autocarwash.subscription.dto.response;

import com.swp.autocarwash.subscription.entity.enums.PlanType;
import com.swp.autocarwash.subscription.entity.enums.SubscriptionPlanStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPlanDetailResponse {

    private Integer id;

    private String planName;

    private BigDecimal price;

    private Integer durationDays;

    private String description;

    private Integer servicePackageId;

    private PlanType planType;

    private Integer maxVehicleCount;

    private SubscriptionPlanStatus status;
}
