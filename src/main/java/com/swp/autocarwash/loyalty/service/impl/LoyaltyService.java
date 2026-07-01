package com.swp.autocarwash.loyalty.service.impl;


import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.booking.event.BookingCompletedEvent;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.loyalty.entity.CustomerTier;
import com.swp.autocarwash.loyalty.entity.LoyaltyPointBalance;
import com.swp.autocarwash.loyalty.entity.LoyaltyPointTransaction;
import com.swp.autocarwash.loyalty.entity.enums.LoyaltyPointTransactionStatus;
import com.swp.autocarwash.loyalty.port.CustomerPort;
import com.swp.autocarwash.loyalty.repository.CustomerTierRepository;
import com.swp.autocarwash.loyalty.repository.LoyaltyPointBalanceRepository;
import com.swp.autocarwash.loyalty.repository.LoyaltyPointTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoyaltyService {

    private final LoyaltyPointTransactionRepository loyaltyPointTransactionRepository;
    private final LoyaltyPointBalanceRepository loyaltyPointBalanceRepository;
    private final CustomerTierRepository customerTierRepository;
    private final CustomerPort customerPort;

    //    sử lý cộng điểm sau khi hoàn thành hooking
    @Transactional
    public void earnPoint(BookingCompletedEvent event) {

        LoyaltyPointBalance balance = getBalance(event);


//        lấy điểm nhân của cutsomer
        BigDecimal multiple = event.pointMultiple();

        int earnedPoint = calculatePoint(event.totalAmount(), multiple);

        int newBalance = balance.getTotalPoints() + earnedPoint;

        balance.setTotalPoints(newBalance);

        balance.setAccumulatedPoints(balance.getAccumulatedPoints() + earnedPoint);

        loyaltyPointBalanceRepository.save(balance);

        LoyaltyPointTransaction transaction = createTransaction(event, earnedPoint, newBalance);

        loyaltyPointTransactionRepository.save(transaction);

        processTierUpgrade(event.customerId(), event.customerTierId(), balance);
    }


    //    taọ loyalty point transaction
    private LoyaltyPointTransaction createTransaction(
            BookingCompletedEvent event,
            Integer earnedPoint,
            Integer newBalance
    ) {
        return LoyaltyPointTransaction.builder()
                .customer(Customer.builder().id(event.customerId()).build())
                .booking(Booking.builder().id(event.bookingId()).build())
                .transactionType(LoyaltyPointTransactionStatus.EARN.toString())
                .points(earnedPoint)
                .balanceAfter(newBalance)
                .createdAt(LocalDateTime.now())
                .build();
    }

    //  lấy loyalty point balance của customer
    private LoyaltyPointBalance getBalance(BookingCompletedEvent event) {
        return loyaltyPointBalanceRepository
                .findLoyaltyPointBalanceByCustomerId(event.customerId())
                .orElseThrow( ()-> new BusinessException(ErrorCode.LOYALTY_POINT_BALANCE_NOT_FOUND));
    }

    //    tỉnh điểm thưởng
    private int calculatePoint(
            BigDecimal amount,
            BigDecimal pointMultiple
    ) {

        BigDecimal basePoint = amount.divide(BigDecimal.valueOf(1000));

        return basePoint.multiply(pointMultiple).intValue();

    }

    //    sử lý tăng hạn
    public void processTierUpgrade(
            Long customerId,
            Integer customerTierId,
            LoyaltyPointBalance balance
    ) {

        CustomerTier newTier =
                customerTierRepository
                        .findFirstByMinPointsLessThanEqualOrderByMinPointsDesc(
                                balance.getAccumulatedPoints()
                        )
                        .orElseThrow(
                                () -> new BusinessException(ErrorCode.CUSTOMER_TIER_NOT_FOUND)
                        );

        CustomerTier currentTier =
                customerTierRepository.findById(customerTierId).orElseThrow(
                        () -> new BusinessException(ErrorCode.CUSTOMER_TIER_NOT_FOUND)
                );

        if (newTier.getMinPoints() > currentTier.getMinPoints()) {
            customerPort.updateCustomerTier(customerId, newTier.getId());
        }

    }
}
