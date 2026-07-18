package com.swp.autocarwash.loyalty.service;

import com.swp.autocarwash.booking.event.BookingCompletedEvent;
import com.swp.autocarwash.loyalty.dto.response.LoyaltyOverviewResponse;
import com.swp.autocarwash.loyalty.dto.response.LoyaltyTransactionPageResponse;
import com.swp.autocarwash.loyalty.dto.response.PointsConversionPreview;
import com.swp.autocarwash.payment.event.SubscriptionInvoicePaidEvent;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface LoyaltyService {

    LoyaltyOverviewResponse getOverview();

    LoyaltyTransactionPageResponse getTransactions(Pageable pageable);

    void earnPoint(BookingCompletedEvent event);

    void earnPointForSubscription(SubscriptionInvoicePaidEvent event);

    /**
     * Quy đổi tiền cọc của booking bị hủy thành điểm tích lũy, cộng ngay vào tài khoản khách hàng.
     *
     * @return số điểm đã cộng
     */
    int earnPointsFromDepositRefund(Long customerId, Long bookingId, BigDecimal amount);

    /**
     * Tính trước (không cộng điểm thật) số điểm khách sẽ nhận nếu quy đổi {@code amount}
     * theo hạng hiện tại — dùng cho màn hình xác nhận hủy TRƯỚC khi khách chọn phương thức hoàn cọc.
     */
    PointsConversionPreview previewPointsForAmount(Long customerId, BigDecimal amount);
}
