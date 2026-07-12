package com.swp.autocarwash.loyalty.adapter;

import com.swp.autocarwash.booking.port.LoyaltyPort;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.loyalty.entity.enums.LoyaltyPointTransactionType;
import com.swp.autocarwash.loyalty.repository.LoyaltyPointBalanceRepository;
import com.swp.autocarwash.loyalty.repository.LoyaltyPointTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoyaltyBookingPort implements LoyaltyPort {

    private final LoyaltyPointBalanceRepository loyaltyPointBalanceRepository;
    private final LoyaltyPointTransactionRepository loyaltyPointTransactionRepository;

    @Override
    public Integer getLotaltyPoint(Long customerId) {
        return loyaltyPointBalanceRepository.findLoyaltyPointBalanceByCustomerId(customerId).orElseThrow( ()-> new BusinessException(ErrorCode.LOYALTY_POINT_BALANCE_NOT_FOUND)).getTotalPoints();
    }

    @Override
    public Integer getEarnedPointForBooking(Long bookingId) {
        return loyaltyPointTransactionRepository.findByBooking_Id(bookingId).stream()
                .filter(t -> t.getTransactionType() == LoyaltyPointTransactionType.EARN)
                .mapToInt(t -> t.getPoints() == null ? 0 : t.getPoints())
                .sum();
    }

    @Override
    public Integer getRedeemedPointForBooking(Long bookingId) {
        int sum = loyaltyPointTransactionRepository.findByBooking_Id(bookingId).stream()
                .filter(t -> t.getTransactionType() == LoyaltyPointTransactionType.REDEEM)
                .mapToInt(t -> t.getPoints() == null ? 0 : t.getPoints())
                .sum();
        return Math.abs(sum);
    }
}
