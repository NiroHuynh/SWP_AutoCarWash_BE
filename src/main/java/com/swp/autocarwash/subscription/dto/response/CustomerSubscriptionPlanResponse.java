package com.swp.autocarwash.subscription.dto.response;

import com.swp.autocarwash.subscription.entity.enums.PlanType;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSubscriptionPlanResponse {

    private Integer id;

    private String planName;

    private BigDecimal price;

    private Integer durationDays;

    private PlanType planType;

    private String servicePackageName;

    private Integer maxVehicleCount;

    private String description;

    // Add-on đi kèm gói - hiển thị cho khách biết quyền lợi khi browse plan.
    private List<String> addonNames;
}
