package com.swp.autocarwash.payment.event;

import com.swp.autocarwash.booking.event.BookingDepositPaidEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Bọc lại {@link ApplicationEventPublisher} của Spring thành publisher riêng
 * của module payment (theo mẫu {@code BookingEventPublisher}), để service
 * thanh toán không phụ thuộc trực tiếp vào Spring API và dễ mock khi test.
 *
 * <p>Các event phát ra khi thanh toán thành công:
 * <ul>
 *   <li>{@link BookingDepositPaidEvent} — cọc booking được xác nhận
 *       (webhook SePay hoặc manual confirm), booking chuyển CONFIRMED.</li>
 *   <li>{@link SubscriptionInvoicePaidEvent} — hóa đơn mua gói được thanh
 *       toán, module subscription lắng nghe để kích hoạt gói.</li>
 * </ul>
 *
 * @author Ngân
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class PaymentEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishBookingDepositPaid(BookingDepositPaidEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    public void publishSubscriptionInvoicePaid(SubscriptionInvoicePaidEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
