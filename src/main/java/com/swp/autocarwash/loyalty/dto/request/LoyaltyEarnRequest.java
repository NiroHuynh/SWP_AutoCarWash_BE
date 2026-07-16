package com.swp.autocarwash.loyalty.dto.request;

import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.loyalty.entity.enums.LoyaltySourceType;
import com.swp.autocarwash.payment.entity.SubscriptionInvoice;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoyaltyEarnRequest {

    private Long customerId;

    private Integer customerTierId;

    private Integer earnedPoint;

    private LoyaltySourceType sourceType;

    private Booking booking;

    private SubscriptionInvoice subscriptionInvoice;

}
