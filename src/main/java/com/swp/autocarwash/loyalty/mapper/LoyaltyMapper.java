package com.swp.autocarwash.loyalty.mapper;

import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.loyalty.dto.response.LoyaltyPointTransactionResponse;
import com.swp.autocarwash.loyalty.dto.response.TierHistoryResponse;
import com.swp.autocarwash.loyalty.dto.response.TierResponse;
import com.swp.autocarwash.loyalty.entity.CustomerTier;
import com.swp.autocarwash.loyalty.entity.CustomerTierHistory;
import com.swp.autocarwash.loyalty.entity.LoyaltyPointTransaction;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.List;

/**
 * Mapper viet tay chuyen entity loyalty sang DTO response.
 *
 * @author KimNgan
 * @version 1.0
 */
@Component
public class LoyaltyMapper {

    private static final ZoneId ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    public LoyaltyPointTransactionResponse toTransactionResponse(LoyaltyPointTransaction txn) {
        Booking booking = txn.getBooking();
        return LoyaltyPointTransactionResponse.builder()
                .id(txn.getId())
                .transactionType(String.valueOf(txn.getTransactionType()))
                .points(txn.getPoints())
                .balanceAfter(txn.getBalanceAfter())
                .bookingId(booking == null ? null : booking.getId())
                .bookingCheckOutAt(booking == null ? null : booking.getCheckOutAt())
                .servicePackageName(booking == null ? null : booking.getServicePackage().getName())
                .createdAt(txn.getCreatedAt() == null ? null : txn.getCreatedAt().atZone(ZONE).toInstant())
                .build();
    }

    public TierHistoryResponse toTierHistoryResponse(CustomerTierHistory history) {
        CustomerTier oldTier = history.getOldTier();
        Booking booking = history.getBooking();
        return TierHistoryResponse.builder()
                .id(history.getId())
                .oldTierName(oldTier == null ? null : oldTier.getTierName())
                .newTierName(history.getNewTier().getTierName())
                .valueAtTransition(history.getValueAtTransition())
                .changeType(history.getChangeType())
                .bookingId(booking == null ? null : booking.getId())
                .createdAt(history.getCreatedAt())
                .build();
    }

    public TierResponse toTierResponse(CustomerTier tier, List<String> benefits) {
        return TierResponse.builder()
                .tierName(tier.getTierName())
                .minPoints(tier.getMinPoints())
                .bookingWindowDays(tier.getBookingWindowDays())
                .pointMultiple(tier.getPointMultiple())
                .retentionTargetAmount(tier.getRetentionTargetAmount())
                .benefits(benefits)
                .build();
    }
}
