package com.swp.autocarwash.subscription.dto.response;


import com.swp.autocarwash.subscription.entity.enums.PlanType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPlanResponse {

    private String planName;

    private BigDecimal price;

    private Integer durationDays;

    private PlanType planType;

    private String description;

    private Integer maxVehicleCount;

    private String servicePackageName;

    // Tên các add-on đi kèm gói (marketing/quyền lợi) - riêng biệt với servicePackageName ở trên.
    private List<String> addonNames;

    private String status;
}
