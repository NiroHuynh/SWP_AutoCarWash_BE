package com.swp.autocarwash.booking.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Bọc lại {@link ApplicationEventPublisher} của Spring thành một publisher riêng
 * của module booking, để BookingServiceImpl không phụ thuộc trực tiếp vào Spring API
 * và có thể mock dễ dàng khi viết unit test.
 */
@Component
@RequiredArgsConstructor
public class BookingEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishBookingCanceled(BookingCanceledEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    public void publicBookingCompleted(BookingCompletedEvent event){
        applicationEventPublisher.publishEvent(event);
    }
}
