package com.swp.autocarwash.loyalty.event.listener;


import com.swp.autocarwash.booking.event.BookingCompletedEvent;
import com.swp.autocarwash.loyalty.service.impl.LoyaltyService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class LoyaltyEnventListener {

    private final LoyaltyService loyaltyService;

    @EventListener
    public void handle(BookingCompletedEvent event){
        loyaltyService.earnPoint(event);
    }
}
