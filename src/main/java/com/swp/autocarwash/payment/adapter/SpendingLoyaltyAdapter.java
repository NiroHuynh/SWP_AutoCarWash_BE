package com.swp.autocarwash.payment.adapter;

import com.swp.autocarwash.loyalty.port.SpendingPort;
import com.swp.autocarwash.payment.repository.BookingInvoiceRepository;
import com.swp.autocarwash.payment.repository.SubscriptionInvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Adapter (module payment) cung cap tong chi tieu cho module loyalty qua {@link SpendingPort}.
 *
 * <p>Cong ca 2 nguon da thanh toan: hoa don booking ({@code BookingInvoice.finalAmount})
 * va hoa don subscription ({@code SubscriptionInvoice.planPrice}).</p>
 *
 * @author KimNgan
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class SpendingLoyaltyAdapter implements SpendingPort {

    private final BookingInvoiceRepository bookingInvoiceRepository;
    private final SubscriptionInvoiceRepository subscriptionInvoiceRepository;

    @Override
    public BigDecimal getTotalSpending(Long customerId, LocalDateTime from, LocalDateTime to) {
        BigDecimal bookingSum = bookingInvoiceRepository.sumFinalAmountPaidBetween(customerId, from, to);
        BigDecimal subscriptionSum = subscriptionInvoiceRepository.sumPlanPricePaidBetween(customerId, from, to);
        return nvl(bookingSum).add(nvl(subscriptionSum));
    }

    private BigDecimal nvl(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
