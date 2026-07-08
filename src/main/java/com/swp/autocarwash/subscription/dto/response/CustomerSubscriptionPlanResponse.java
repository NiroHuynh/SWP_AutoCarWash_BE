package com.swp.autocarwash.subscription.dto.response;

import com.swp.autocarwash.subscription.entity.enums.PlanType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSubscriptionPlanResponse {

    private String planName;

    private BigDecimal price;

    private Integer durationDays;

    private PlanType planType;

    private String servicePackageName;

    private Integer maxVehicleCount;

    private String description;
}
