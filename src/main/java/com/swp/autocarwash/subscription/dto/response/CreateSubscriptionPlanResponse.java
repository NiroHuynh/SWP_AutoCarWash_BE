package com.swp.autocarwash.subscription.dto.response;

import com.swp.autocarwash.subscription.entity.enums.PlanType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSubscriptionPlanResponse {

    private Integer id;

    private String planName;

    private BigDecimal price;

    private Integer durationDays;

    private String description;

    private Integer servicePackageId;

    private String servicePackageName;

    private PlanType planType;

    private Integer maxVehicleCount;
}
