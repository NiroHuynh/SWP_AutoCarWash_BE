package com.swp.autocarwash.loyalty.service.impl;


import com.swp.autocarwash.auth.util.SecurityUtils;
import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.booking.event.BookingCompletedEvent;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.repository.CustomerRepository;
import com.swp.autocarwash.loyalty.dto.response.LoyaltyOverviewResponse;
import com.swp.autocarwash.loyalty.dto.response.LoyaltyTransactionPageResponse;
import com.swp.autocarwash.loyalty.entity.CustomerTier;
import com.swp.autocarwash.loyalty.entity.LoyaltyPointBalance;
import com.swp.autocarwash.loyalty.entity.LoyaltyPointTransaction;
import com.swp.autocarwash.loyalty.entity.TierBenefit;
import com.swp.autocarwash.loyalty.entity.enums.LoyaltyPointTransactionType;
import com.swp.autocarwash.loyalty.entity.enums.LoyaltySourceType;
import com.swp.autocarwash.loyalty.port.CustomerPort;
import com.swp.autocarwash.loyalty.repository.CustomerTierRepository;
import com.swp.autocarwash.loyalty.repository.LoyaltyPointBalanceRepository;
import com.swp.autocarwash.loyalty.repository.LoyaltyPointTransactionRepository;
import com.swp.autocarwash.loyalty.service.LoyaltyProfileService;
import com.swp.autocarwash.loyalty.service.LoyaltyService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoyaltyServiceImpl implements LoyaltyService {

    private final LoyaltyPointTransactionRepository loyaltyPointTransactionRepository;
    private final LoyaltyPointBalanceRepository loyaltyPointBalanceRepository;
    private final CustomerTierRepository customerTierRepository;
    private final CustomerRepository customerRepository;
    private final CustomerPort customerPort;
    private final LoyaltyProfileService loyaltyProfileService;
    private final SecurityUtils securityUtils;
    private final ModelMapper modelMapper;

    //    sử lý cộng điểm sau khi hoàn thành hooking
    @Transactional
    public void earnPoint(BookingCompletedEvent event) {

        LoyaltyPointBalance balance = getBalance(event);

//        điểm tích luỹ trước khi cộng (dùng để xác định lên/xuống hạng)
        int previousAccumulated = balance.getAccumulatedPoints();

//        lấy điểm nhân của cutsomer
        BigDecimal multiple = event.pointMultiple();

        int earnedPoint = calculatePoint(event.totalAmount(), multiple);

        int newBalance = balance.getTotalPoints() + earnedPoint;

        balance.setTotalPoints(newBalance);

        balance.setAccumulatedPoints(balance.getAccumulatedPoints() + earnedPoint);

        int newAccumulated = balance.getAccumulatedPoints();

        loyaltyPointBalanceRepository.save(balance);

        LoyaltyPointTransaction transaction = createTransaction(event, earnedPoint, newBalance);

        loyaltyPointTransactionRepository.save(transaction);

        processTierUpgrade(event.customerId(), event.customerTierId(), balance);

//        ghi lịch sử lên/xuống hạng (nếu hạng thay đổi)
        loyaltyProfileService.recordTierTransitionIfChanged(
                event.customerId(), previousAccumulated, newAccumulated, event.bookingId());
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
                .transactionType(LoyaltyPointTransactionType.EARN)
                .points(earnedPoint)
                .balanceAfter(newBalance)
                .createdAt(LocalDateTime.now())
                .sourceType(LoyaltySourceType.BOOKING)
                .build();
    }

    //  lấy loyalty point balance của customer
    private LoyaltyPointBalance getBalance(BookingCompletedEvent event) {
        return loyaltyPointBalanceRepository
                .findLoyaltyPointBalanceByCustomerId(event.customerId())
                .orElseThrow(() -> new BusinessException(ErrorCode.LOYALTY_POINT_BALANCE_NOT_FOUND));
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
    private void processTierUpgrade(
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


    @Override
    public LoyaltyOverviewResponse getOverview() {
        Customer customer = getCustomer();
        LoyaltyPointBalance balance = loyaltyPointBalanceRepository
                .findLoyaltyPointBalanceByCustomerId(customer.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.LOYALTY_POINT_BALANCE_NOT_FOUND));
        List<CustomerTier> tiers = customerTierRepository.findAllByOrderByMinPointsAsc();
        List<LoyaltyPointTransaction> transactions = loyaltyPointTransactionRepository
                .findTop5ByCustomer_IdOrderByCreatedAtDesc(customer.getId());

        LoyaltyOverviewResponse.Balance balancedto = getBalanceDTO(balance.getTotalPoints());
        LoyaltyOverviewResponse.Progress progressdto = getProgress(
                customer.getCustomerTier(),
                tiers,
                balance
        );

        List<LoyaltyOverviewResponse.Tier> tierdto = getTiers(tiers, customer.getCustomerTier());
        List<LoyaltyOverviewResponse.RecentTransaction> recentTransactionsdto =
                transactions.stream()
                        .map(this::mapTransaction)
                        .toList();

        return LoyaltyOverviewResponse.builder()
                .balance(balancedto)
                .progress(progressdto)
                .tiers(tierdto)
                .recentTransactions(recentTransactionsdto)
                .build();
    }

    private List<LoyaltyOverviewResponse.Tier> getTiers(
            List<CustomerTier> tiers,
            CustomerTier currentTier
    ) {
        List<LoyaltyOverviewResponse.Tier> tierdto = new ArrayList<>();
        for (CustomerTier tier : tiers) {
            LoyaltyOverviewResponse.Tier tierItem = LoyaltyOverviewResponse.Tier.builder()
                    .id(tier.getId())
                    .tierName(tier.getTierName())
                    .minPoints(tier.getMinPoints())
                    .pointMultiple(tier.getPointMultiple())
                    .isCurrent(currentTier.getId().equals(tier.getId()))
                    .benefits(tier.getTierBenefits()
                            .stream()
                            .map(TierBenefit::getBenefitDescription)
                            .toList()
                    )
                    .build();
            tierdto.add(tierItem);
        }
        return tierdto;
    }

    private LoyaltyOverviewResponse.Progress getProgress(
            CustomerTier currentTier,
            List<CustomerTier> tiers,
            LoyaltyPointBalance balance
    ) {
        CustomerTier nextTier = tiers.stream()
                .filter(t -> t.getMinPoints() > balance.getTotalPoints())
                .findFirst()
                .orElse(null);

        Integer pointsToNext = null;

        if (nextTier != null) {

            pointsToNext =
                    nextTier.getMinPoints() - balance.getTotalPoints();

        }

        return LoyaltyOverviewResponse.Progress.builder()
                .currentTierName(currentTier.getTierName())
                .nextTierName(nextTier.getTierName())
                .pointsToNextTier(pointsToNext)
                .progressPoints(balance.getTotalPoints())
                .build();
    }

    private LoyaltyOverviewResponse.Balance getBalanceDTO(Integer totalPoints) {
        return LoyaltyOverviewResponse.Balance.builder()
                .totalPoints(totalPoints)
                .build();
    }

    @Override
    public LoyaltyTransactionPageResponse getTransactions(Pageable pageable) {
        Customer customer = getCustomer();

        Page<LoyaltyPointTransaction> page =
                loyaltyPointTransactionRepository.findByCustomer_IdOrderByCreatedAtDesc(
                        customer.getId(),
                        pageable);

        List<LoyaltyOverviewResponse.RecentTransaction> transactions =
                page.getContent()
                .stream()
                .map(this::mapTransaction)
                .toList();


        return  LoyaltyTransactionPageResponse.builder()
                .content(
                        transactions.stream()
                                .map(transaction ->
                                        modelMapper.map(transaction,LoyaltyTransactionPageResponse.Transaction.class)
                                )
                                .toList()
                )
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    private LoyaltyOverviewResponse.RecentTransaction mapTransaction(
            LoyaltyPointTransaction transaction
    ) {
        LoyaltyPointTransactionType transactionType =
                transaction.getTransactionType().equals(LoyaltyPointTransactionType.EARN)
                        ? LoyaltyPointTransactionType.EARN : LoyaltyPointTransactionType.REDEEM;

        return LoyaltyOverviewResponse.RecentTransaction.builder()
                .id(transaction.getId())
                .createdAt(transaction.getCreatedAt())
                .transactionType(transactionType)
                .source(transaction.getSourceType().name())
                .subscriptionInvoiceId(
                        transaction.getSubscriptionInvoice() != null
                                ? transaction.getSubscriptionInvoice().getId()
                                : null
                )
                .bookingId(
                        transaction.getBooking() != null
                                ? transaction.getBooking().getId()
                                : null
                )
                .points(transaction.getPoints())
                .build();
    }

    Customer getCustomer() {
        Long userId = securityUtils.getCurrentUserId();
        Customer customer = customerRepository.findByUserId(userId);
        if (customer == null) throw new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND);
        return customer;
    }
}
