package com.swp.autocarwash.loyalty.service.impl;

import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.loyalty.entity.CustomerTier;
import com.swp.autocarwash.loyalty.entity.LoyaltyPointBalance;
import com.swp.autocarwash.loyalty.entity.LoyaltyPointTransaction;
import com.swp.autocarwash.loyalty.entity.enums.LoyaltyPointTransactionType;
import com.swp.autocarwash.loyalty.entity.enums.LoyaltySourceType;
import com.swp.autocarwash.loyalty.port.CustomerPort;
import com.swp.autocarwash.loyalty.port.SpendingPort;
import com.swp.autocarwash.loyalty.repository.CustomerTierRepository;
import com.swp.autocarwash.loyalty.repository.LoyaltyPointBalanceRepository;
import com.swp.autocarwash.loyalty.repository.LoyaltyPointTransactionRepository;
import com.swp.autocarwash.loyalty.service.AnnualTierEvaluationService;
import com.swp.autocarwash.loyalty.service.LoyaltyProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Danh gia lai hang thanh vien theo chu ky nam (BR-FE-44) va reset diem cho chu ky moi.
 *
 * @author KimNgan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AnnualTierEvaluationServiceImpl implements AnnualTierEvaluationService {

    private final LoyaltyPointBalanceRepository balanceRepository;
    private final LoyaltyPointTransactionRepository transactionRepository;
    private final CustomerTierRepository customerTierRepository;
    private final CustomerPort customerPort;
    private final LoyaltyProfileService loyaltyProfileService;
    private final SpendingPort spendingPort;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void evaluateAndResetOneCustomer(Long customerId, LocalDateTime yearStart, LocalDateTime yearEnd) {
        LoyaltyPointBalance balance = balanceRepository.findLoyaltyPointBalanceByCustomerId(customerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.LOYALTY_POINT_BALANCE_NOT_FOUND));

        BigDecimal spending = spendingPort.getTotalSpending(customerId, yearStart, yearEnd);

        // Xet nang/ha hang truc tiep tu tien chi tieu that so voi retention_target_amount
        // cua tung hang (KHONG quy doi qua diem/min_points) - dung theo yeu cau BR-FE-44.
        CustomerTier newTier = customerTierRepository
                .findFirstByRetentionTargetAmountLessThanEqualOrderByRetentionTargetAmountDesc(spending)
                .orElseThrow(() -> new BusinessException(ErrorCode.CUSTOMER_TIER_NOT_FOUND));

        int previousAccumulated = balance.getAccumulatedPoints() == null ? 0 : balance.getAccumulatedPoints();
        int previousTotal = balance.getTotalPoints() == null ? 0 : balance.getTotalPoints();
        // Hang cu phai lay tu FK thuc te cua customer, KHONG duoc suy tu previousAccumulated:
        // sau lan danh gia dau tien accumulatedPoints da ve 0, neu job chay lai (vd retry, hoac
        // cron test) thi previousAccumulated luon la 0 -> se suy nham hang cu la MEMBER moi lan,
        // gay ghi lich su UPGRADE trung lap dù hang khong doi.
        CustomerTier oldTier = balance.getCustomer().getCustomerTier();

        // Persist FK hang moi (co the UPGRADE hoac DOWNGRADE, khac voi flow checkout chi nang)
        customerPort.updateCustomerTier(customerId, newTier.getId());

        // Ghi lich su neu hang thay doi; tu no-op neu giu nguyen (khong ghi MAINTAIN)
        // valueAtTransition luu tong tien chi tieu (VND) tai thoi diem xet, khop voi
        // tieu chi retention_target_amount dung de quyet dinh newTier o tren.
        loyaltyProfileService.recordTierChangeIfDifferent(
                customerId, oldTier, newTier, spending.intValue(), null);

        // Balance da o 0/0 tu lan reset truoc (vd job chay lai trong cung ngay) -> khong co gi
        // de reset, bo qua de tranh ghi rac dong RESET voi points=0.
        if (previousTotal != 0 || previousAccumulated != 0) {
            // Ghi 1 dong RESET de bao toan audit truoc khi dua so du ve 0
            LoyaltyPointTransaction txn = new LoyaltyPointTransaction();
            txn.setCustomer(balance.getCustomer());
            txn.setBooking(null);
            txn.setSourceType(LoyaltySourceType.ADJUSTMENT);
            txn.setTransactionType(LoyaltyPointTransactionType.RESET);
            txn.setPoints(-previousTotal);
            txn.setBalanceAfter(0);
            txn.setCreatedAt(LocalDateTime.now());
            transactionRepository.save(txn);

            // Reset ca totalPoints va accumulatedPoints ve 0 - bat dau chu ky tich luy moi
            balance.setTotalPoints(0);
            balance.setAccumulatedPoints(0);
            balanceRepository.save(balance);
        }

        log.info("[AnnualTierEvaluationService] customerId={} spending={} newTier={}",
                customerId, spending, newTier.getTierName());
    }
}
