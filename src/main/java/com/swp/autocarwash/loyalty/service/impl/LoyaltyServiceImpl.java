package com.swp.autocarwash.loyalty.service.impl;


import com.swp.autocarwash.auth.util.SecurityUtils;
import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.booking.event.BookingCompletedEvent;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.repository.CustomerRepository;
import com.swp.autocarwash.loyalty.dto.request.LoyaltyEarnRequest;
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
import com.swp.autocarwash.payment.entity.SubscriptionInvoice;
import com.swp.autocarwash.payment.event.SubscriptionInvoicePaidEvent;
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

        LoyaltyEarnRequest request =
                LoyaltyEarnRequest.builder()
                        .customerId(event.customerId())
                        .customerTierId(event.customerTierId())
                        .earnedPoint(
                                calculatePoint(
                                        event.totalAmount(),
                                        event.pointMultiple()
                                )
                        )
                        .sourceType(LoyaltySourceType.BOOKING)
                        .booking(
                                Booking.builder()
                                        .id(event.bookingId())
                                        .build()
                        )
                        .build();

        doEarnPoint(request);
    }


    @Transactional
    public void earnPointForSubscription(
            SubscriptionInvoicePaidEvent event
    ) {

        Customer customer = customerRepository.findById(event.getCustomerId()).orElseThrow(
                () -> new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND));

        LoyaltyEarnRequest request =
                LoyaltyEarnRequest.builder()
                        .customerId(event.getCustomerId())
                        .customerTierId(customer.getCustomerTier().getId())
                        .earnedPoint(
                                calculatePoint(
                                        event.getPlanPrice(),
                                        customer.getCustomerTier().getPointMultiple()
                                )
                        )
                        .sourceType(LoyaltySourceType.SUBSCRIPTION)
                        .subscriptionInvoice(
                                SubscriptionInvoice.builder()
                                        .id(
                                               event.getInvoiceId()
                                        )
                                        .build()
                        )
                        .build();

        doEarnPoint(request);
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


    @Transactional
    protected void doEarnPoint(
            LoyaltyEarnRequest request
    ) {

        LoyaltyPointBalance balance =
                getBalance(request.getCustomerId());

        int previousAccumulated =
                balance.getAccumulatedPoints();

        int newBalance =
                balance.getTotalPoints()
                        + request.getEarnedPoint();

        balance.setTotalPoints(newBalance);

        balance.setAccumulatedPoints(
                balance.getAccumulatedPoints()
                        + request.getEarnedPoint());

        int newAccumulated =
                balance.getAccumulatedPoints();

        loyaltyPointBalanceRepository.save(balance);

        LoyaltyPointTransaction transaction =
                createTransaction(
                        request,
                        newBalance);

        loyaltyPointTransactionRepository.save(transaction);

        processTierUpgrade(
                request.getCustomerId(),
                request.getCustomerTierId(),
                balance);

        loyaltyProfileService.recordTierTransitionIfChanged(
                request.getCustomerId(),
                previousAccumulated,
                newAccumulated,
                request.getBooking() != null
                        ? request.getBooking().getId()
                        : null
        );
    }

    private LoyaltyPointBalance getBalance(
            Long customerId
    ) {

        return loyaltyPointBalanceRepository
                .findLoyaltyPointBalanceByCustomerId(customerId)
                .orElseThrow(() ->
                        new BusinessException(
                                ErrorCode.LOYALTY_POINT_BALANCE_NOT_FOUND));
    }

    private LoyaltyPointTransaction createTransaction(
            LoyaltyEarnRequest request,
            Integer balanceAfter
    ) {

        return LoyaltyPointTransaction.builder()
                .customer(
                        Customer.builder()
                                .id(request.getCustomerId())
                                .build()
                )
                .booking(request.getBooking())
                .subscriptionInvoice(
                        request.getSubscriptionInvoice()
                )
                .transactionType(
                        LoyaltyPointTransactionType.EARN
                )
                .sourceType(request.getSourceType())
                .points(request.getEarnedPoint())
                .balanceAfter(balanceAfter)
                .createdAt(LocalDateTime.now())
                .build();
    }

    //    tỉnh điểm thưởng
    private int calculatePoint(
            BigDecimal amount,
            BigDecimal pointMultiple
    ) {

        BigDecimal basePoint = amount.divide(BigDecimal.valueOf(1000));

        return basePoint.multiply(pointMultiple).intValue();

    }
}
