package com.swp.autocarwash.loyalty.adapter;

import com.swp.autocarwash.booking.port.LoyaltyPort;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.loyalty.repository.LoyaltyPointBalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class LoyaltyBookingPort implements LoyaltyPort {

    private final LoyaltyPointBalanceRepository loyaltyPointBalanceRepository;

    @Override
    public Integer getLotaltyPoint(Long customerId) {
        return loyaltyPointBalanceRepository.findLoyaltyPointBalanceByCustomerId(customerId).orElseThrow( ()-> new BusinessException(ErrorCode.LOYALTY_POINT_BALANCE_NOT_FOUND)).getTotalPoints();
    }
}
