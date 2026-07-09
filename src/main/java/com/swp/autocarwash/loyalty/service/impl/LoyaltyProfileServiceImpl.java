package com.swp.autocarwash.loyalty.service.impl;

import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.booking.repository.BookingRepository;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.ResourceNotFoundException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.repository.CustomerRepository;
import com.swp.autocarwash.loyalty.dto.response.LoyaltyHistoryResponse;
import com.swp.autocarwash.loyalty.dto.response.LoyaltyPointTransactionResponse;
import com.swp.autocarwash.loyalty.dto.response.LoyaltyProfileResponse;
import com.swp.autocarwash.loyalty.dto.response.TierHistoryResponse;
import com.swp.autocarwash.loyalty.dto.response.TierResponse;
import com.swp.autocarwash.loyalty.entity.CustomerTier;
import com.swp.autocarwash.loyalty.entity.CustomerTierHistory;
import com.swp.autocarwash.loyalty.entity.enums.TierChangeType;
import com.swp.autocarwash.loyalty.entity.LoyaltyPointBalance;
import com.swp.autocarwash.loyalty.entity.TierBenefit;
import com.swp.autocarwash.loyalty.mapper.LoyaltyMapper;
import com.swp.autocarwash.loyalty.port.SpendingPort;
import com.swp.autocarwash.loyalty.repository.*;
import com.swp.autocarwash.loyalty.service.LoyaltyProfileService;
import com.swp.autocarwash.system.service.SystemSettingService;
import com.swp.autocarwash.system.service.impl.SystemSettingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Xu ly nghiep vu xem diem loyalty, ngay reset, chi tieu va lich su diem (FE-42-US-01).
 *
 * @author KimNgan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class LoyaltyProfileServiceImpl implements LoyaltyProfileService {

    private static final ZoneId ZONE = ZoneId.of("Asia/Ho_Chi_Minh");
    private static final String DEFAULT_TIER_NAME = "MEMBER";
    private static final String RETENTION_IN_PROGRESS = "IN_PROGRESS";

    private final LoyaltyPointBalanceRepository balanceRepository;
    private final LoyaltyPointTransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final BookingRepository bookingRepository;
    private final CustomerTierRepository customerTierRepository;
    private final CustomerTierHistoryRepository tierHistoryRepository;
    private final TierBenefitRepository tierBenefitRepository;
    private final SystemSettingService systemSettingService;
    private final SpendingPort spendingPort;
    private final LoyaltyMapper loyaltyMapper;

    @Override
    @Transactional(readOnly = true)
    public LoyaltyProfileResponse getLoyaltyProfile(Long customerId) {
        LoyaltyPointBalance balance = balanceRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.LOYALTY_BALANCE_NOT_FOUND));
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.CUSTOMER_NOT_FOUND));

        int accumulated = balance.getAccumulatedPoints() == null ? 0 : balance.getAccumulatedPoints();

        // Hang hien tai doc tu FK customer.customer_tier_id: FK nay duoc dong bo moi khi
        // accumulatedPoints thay doi (checkout earn - LoyaltyServiceImpl.processTierUpgrade;
        // va danh gia thuong nien - AnnualTierEvaluationService), nen la nguon dang tin cay hon
        // accumulatedPoints "tuc thoi" - dac biet sau khi job thuong nien reset accumulatedPoints
        // ve 0 nhung hang van phai giu nguyen gia tri vua danh gia.
        CustomerTier currentTier = customer.getCustomerTier();
        String currentTierName = currentTier == null ? DEFAULT_TIER_NAME : currentTier.getTierName();

        LoyaltyProfileResponse.LoyaltyProfileResponseBuilder builder = LoyaltyProfileResponse.builder()
                .totalPoints(balance.getTotalPoints() == null ? 0 : balance.getTotalPoints())
                .accumulatedPoints(accumulated)
                .tierName(currentTierName);

        // Tien do len hang ke tiep - tinh tuong doi tu nguong cua hang HIEN TAI (FK) cong voi
        // accumulatedPoints tich luy trong chu ky nay, thay vi tra tuyet doi tu accumulatedPoints:
        // sau reset thuong nien accumulatedPoints ve 0, tra tuyet doi se bao sai next-tier cho
        // khach dang o Gold/Platinum (vd hien "next tier: Silver" ngay sau khi vua reset).
        int currentTierMinPoints = currentTier == null ? 0 : currentTier.getMinPoints();
        customerTierRepository.findTop1ByMinPointsGreaterThanOrderByMinPointsAsc(currentTierMinPoints)
                .ifPresent(next -> {
                    int pointsNeeded = next.getMinPoints() - currentTierMinPoints;
                    int pointsMissing = Math.max(pointsNeeded - accumulated, 0);
                    builder.nextTierName(next.getTierName());
                    builder.pointsToNextTier(pointsMissing);
                    builder.amountToNextTier(BigDecimal.valueOf(pointsMissing)
                            .multiply(systemSettingService.getDepositAmount(
                                    SystemSettingServiceImpl.LOYALTY_POINT_PER_VND)));
                });

        // Ngay reset thuong nien + countdown
        LocalDate today = LocalDate.now(ZONE);
        LocalDate resetDate = computeUpcomingResetDate(today);
        builder.upcomingResetDate(resetDate)
                .daysUntilReset(ChronoUnit.DAYS.between(today, resetDate));

        // Tong chi tieu nam hien tai (year-to-date)
        LocalDateTime startOfYear = startOfYearDateTime(today.getYear());
        builder.currentTotalSpending(spendingPort.getTotalSpending(customerId, startOfYear, LocalDateTime.now(ZONE)));

        // Tien do giu hang (retention): chu ky = 1 nam, bat dau dung vao ngay reset diem gan nhat
        // va ket thuc vao ngay reset ke tiep. Tinh real-time tu SpendingPort thay vi doc mot cot
        // luu san (khong ai cap nhat), de dam bao luon dung voi du lieu hoa don thuc te.
        LocalDate cycleStart = resetDate.minusYears(1);
        LocalDateTime cycleStartLdt = cycleStart.atStartOfDay();
        BigDecimal retentionCurrent = spendingPort.getTotalSpending(customerId, cycleStartLdt, LocalDateTime.now(ZONE));
        BigDecimal retentionTarget = currentTier == null ? null : currentTier.getRetentionTargetAmount();
        builder.retentionTargetAmount(retentionTarget)
                .retentionCurrentAmount(retentionCurrent)
                .retentionEndDate(resetDate)
                .retentionStatus(RETENTION_IN_PROGRESS);

        return builder.build();
    }

    @Override
    @Transactional(readOnly = true)
    public LoyaltyHistoryResponse getPointHistory(Long customerId, Integer year, Integer month) {
        int resolvedYear = year != null ? year : LocalDate.now(ZONE).getYear();
        if (resolvedYear < 2000 || resolvedYear > 9999) {
            throw new BusinessException(ErrorCode.INVALID_LOYALTY_FILTER);
        }
        if (month != null && (month < 1 || month > 12)) {
            throw new BusinessException(ErrorCode.INVALID_LOYALTY_FILTER);
        }

        // Total spending luon tinh CA NAM (khong doi khi loc thang)
        LocalDateTime yearStart = startOfYearDateTime(resolvedYear);
        LocalDateTime yearEnd = startOfYearDateTime(resolvedYear + 1);
        BigDecimal totalSpending = spendingPort.getTotalSpending(customerId, yearStart, yearEnd);

        // Khoang giao dich: neu co thang -> chi thang do; neu khong -> ca nam
        LocalDateTime txnFrom;
        LocalDateTime txnTo;
        if (month != null) {
            LocalDate monthStart = LocalDate.of(resolvedYear, month, 1);
            txnFrom = monthStart.atStartOfDay();
            txnTo = monthStart.plusMonths(1).atStartOfDay();
        } else {
            txnFrom = yearStart;
            txnTo = yearEnd;
        }

        List<LoyaltyPointTransactionResponse> transactions = transactionRepository
                .findByCustomerAndPeriod(customerId, txnFrom, txnTo)
                .stream()
                .map(loyaltyMapper::toTransactionResponse)
                .toList();

        return LoyaltyHistoryResponse.builder()
                .year(resolvedYear)
                .month(month)
                .totalSpending(totalSpending)
                .transactions(transactions)
                .build();
    }

    /**
     * Ngay reset thuong nien sap toi, dua tren config MM-DD (vd "01-01").
     * Neu ngay reset nam nay da qua HOAC dung la hom nay (job da chay) thi lay nam sau.
     */
    private LocalDate computeUpcomingResetDate(LocalDate today) {
        String mmdd = systemSettingService.getStringValue(SystemSettingServiceImpl.LOYALTY_RESET_MONTH_DAY);
        int month;
        int day;
        try {
            String[] parts = mmdd.trim().split("-");
            month = Integer.parseInt(parts[0]);
            day = Integer.parseInt(parts[1]);
        } catch (RuntimeException e) {
            throw new BusinessException(ErrorCode.INVALID_CONFIG_VALUE_FORMAT);
        }
        LocalDate candidate = LocalDate.of(today.getYear(), month, day);
        if (!candidate.isAfter(today)) {
            candidate = candidate.plusYears(1);
        }
        return candidate;
    }

    private Instant startOfYear(int year) {
        return LocalDate.of(year, 1, 1).atStartOfDay(ZONE).toInstant();
    }

    /** Dau nam duong lich theo gio tuong VN (dung cho query cot LocalDateTime: invoice.paidAt, transaction.createdAt). */
    private LocalDateTime startOfYearDateTime(int year) {
        return LocalDate.of(year, 1, 1).atStartOfDay();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TierResponse> getTierList() {
        List<CustomerTier> tiers = customerTierRepository.findAllByOrderByMinPointsAsc();
        return tiers.stream()
                .map(tier -> {
                    List<String> benefits = tierBenefitRepository
                            .findByCustomerTier_IdOrderByIdAsc(tier.getId())
                            .stream()
                            .map(TierBenefit::getBenefitDescription)
                            .toList();
                    return loyaltyMapper.toTierResponse(tier, benefits);
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TierHistoryResponse> getTierHistory(Long customerId, Integer year, Integer month) {
        int resolvedYear = year != null ? year : LocalDate.now(ZONE).getYear();
        if (resolvedYear < 2000 || resolvedYear > 9999) {
            throw new BusinessException(ErrorCode.INVALID_LOYALTY_FILTER);
        }
        if (month != null && (month < 1 || month > 12)) {
            throw new BusinessException(ErrorCode.INVALID_LOYALTY_FILTER);
        }

        Instant from;
        Instant to;
        if (month != null) {
            LocalDate monthStart = LocalDate.of(resolvedYear, month, 1);
            from = monthStart.atStartOfDay(ZONE).toInstant();
            to = monthStart.plusMonths(1).atStartOfDay(ZONE).toInstant();
        } else {
            from = startOfYear(resolvedYear);
            to = startOfYear(resolvedYear + 1);
        }

        return tierHistoryRepository.findByCustomerAndPeriod(customerId, from, to)
                .stream()
                .map(loyaltyMapper::toTierHistoryResponse)
                .toList();
    }

    @Override
    @Transactional
    public void recordTierTransitionIfChanged(Long customerId, int previousAccumulatedPoints,
                                               int newAccumulatedPoints, Long bookingId) {
        CustomerTier oldTier = customerTierRepository
                .findTop1ByMinPointsLessThanEqualOrderByMinPointsDesc(previousAccumulatedPoints)
                .orElse(null);
        CustomerTier newTier = customerTierRepository
                .findTop1ByMinPointsLessThanEqualOrderByMinPointsDesc(newAccumulatedPoints)
                .orElse(null);
        recordTierChangeIfDifferent(customerId, oldTier, newTier, newAccumulatedPoints, bookingId);
    }

    @Override
    @Transactional
    public void recordTierChangeIfDifferent(Long customerId, CustomerTier oldTier, CustomerTier newTier,
                                             int valueAtTransition, Long bookingId) {
        if (newTier == null || oldTier == null) {
            return; // Chua co hang cu (lan dau) -> khong phai nang/ha hang, khong ghi lich su
        }
        if (oldTier.getId().equals(newTier.getId())) {
            return; // Hang khong doi
        }

        TierChangeType changeType = newTier.getMinPoints() > oldTier.getMinPoints()
                ? TierChangeType.UPGRADE
                : TierChangeType.DOWNGRADE;

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.CUSTOMER_NOT_FOUND));
        Booking booking = bookingId == null ? null : bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BOOKING_NOT_FOUND));
        CustomerTierHistory history = new CustomerTierHistory();
        history.setCustomer(customer);
        history.setOldTier(oldTier);
        history.setNewTier(newTier);
        history.setValueAtTransition(valueAtTransition);
        history.setChangeType(changeType);
        history.setBooking(booking);
        tierHistoryRepository.save(history);
    }
}
