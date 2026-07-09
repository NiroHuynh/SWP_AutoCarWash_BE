package com.swp.autocarwash.subscription.dto.response;


import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RenewalInfoResponse {

    private Long subscriptionId;

    private String planName;

    private Integer durationDays;

    private BigDecimal price;

}
