package com.swp.autocarwash.loyalty.service;

import com.swp.autocarwash.booking.event.BookingCompletedEvent;
import com.swp.autocarwash.loyalty.dto.response.LoyaltyOverviewResponse;
import com.swp.autocarwash.loyalty.dto.response.LoyaltyTransactionPageResponse;
import com.swp.autocarwash.payment.event.SubscriptionInvoicePaidEvent;
import org.springframework.data.domain.Pageable;

public interface LoyaltyService {

    LoyaltyOverviewResponse getOverview();

    LoyaltyTransactionPageResponse getTransactions(Pageable pageable);

    void earnPoint(BookingCompletedEvent event);

    void earnPointForSubscription(SubscriptionInvoicePaidEvent event);
}
