package com.swp.autocarwash.loyalty.event.listener;


import com.swp.autocarwash.booking.event.BookingCompletedEvent;
import com.swp.autocarwash.loyalty.service.impl.LoyaltyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoyaltyEnventListener {

    private final LoyaltyServiceImpl loyaltyService;

    @EventListener
    public void handle(BookingCompletedEvent event){
        loyaltyService.earnPoint(event);
    }
}
