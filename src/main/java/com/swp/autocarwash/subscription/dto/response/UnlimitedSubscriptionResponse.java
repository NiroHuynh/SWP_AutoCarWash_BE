package com.swp.autocarwash.subscription.dto.response;

import com.swp.autocarwash.subscription.entity.enums.SubscriptionStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnlimitedSubscriptionResponse {

    private Long id;

    private String planName;

    private String servicePackageName;

    private SubscriptionStatus status;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer durationDays;

    private BigDecimal price;

    private SubscriptionVehicleResponse vehicle;

    private String description;


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubscriptionVehicleResponse {

        private Long id;

        private String licensePlate;

        private String vehicleName;

    }
}
