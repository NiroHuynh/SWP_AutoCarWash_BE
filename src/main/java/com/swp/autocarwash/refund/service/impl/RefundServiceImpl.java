package com.swp.autocarwash.refund.service.impl;

import com.swp.autocarwash.booking.entity.enums.BookingStatus;
import com.swp.autocarwash.common.contract.booking.RefundableBookingContract;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.ResourceNotFoundException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.refund.dto.request.CreateRefundRequest;
import com.swp.autocarwash.refund.dto.response.AccountLookupResponse;
import com.swp.autocarwash.refund.dto.response.DepositAmountResponse;
import com.swp.autocarwash.refund.dto.response.PointsPreviewResponse;
import com.swp.autocarwash.refund.dto.response.RefundDetailResponse;
import com.swp.autocarwash.refund.dto.response.RefundListPageResponse;
import com.swp.autocarwash.refund.dto.response.RefundResponse;
import com.swp.autocarwash.refund.dto.response.RefundSummaryResponse;
import com.swp.autocarwash.loyalty.dto.response.PointsConversionPreview;
import com.swp.autocarwash.refund.entity.Refund;
import com.swp.autocarwash.refund.entity.enums.BankEnum;
import com.swp.autocarwash.refund.entity.enums.RefundMethod;
import com.swp.autocarwash.refund.entity.enums.RefundStatus;
import com.swp.autocarwash.refund.mapper.RefundMapper;
import com.swp.autocarwash.refund.port.BookingRefundPort;
import com.swp.autocarwash.refund.port.VietQrLookupPort;
import com.swp.autocarwash.refund.repository.RefundRepository;
import com.swp.autocarwash.refund.service.RefundService;
import com.swp.autocarwash.loyalty.service.LoyaltyService;
import com.swp.autocarwash.system.service.SystemSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.net.URLEncoder;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static com.swp.autocarwash.system.service.impl.SystemSettingServiceImpl.REFUND_TRANSFER_CONTENT_TEMPLATE;

/**
 * Triển khai nghiệp vụ hoàn tiền US-04.
 *
 * @author KimNgan
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {

    private static final ZoneId ZONE = ZoneId.of("Asia/Ho_Chi_Minh");
    /** Ngưỡng phút trước giờ hẹn được phép hủy để hoàn tiền (≥2 giờ). */
    private static final long CANCEL_THRESHOLD_MINUTES = 120;

    private final BookingRefundPort bookingRefundPort;
    private final VietQrLookupPort vietQrLookupPort;
    private final RefundRepository refundRepository;
    private final RefundMapper refundMapper;
    private final SystemSettingService systemSettingService;
    private final LoyaltyService loyaltyService;

    @Override
    public AccountLookupResponse lookupAccount(String bin, String accountNumber) {
        BankEnum bank = BankEnum.fromBin(bin)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_BANK));

        String accountName = vietQrLookupPort.lookupAccountName(bin, accountNumber)
                .orElseThrow(() -> new BusinessException(ErrorCode.REFUND_ACCOUNT_LOOKUP_FAILED));

        return AccountLookupResponse.builder()
                .bin(bin)
                .bankName(bank.getDisplayName())
                .accountNumber(accountNumber)
                .accountName(accountName)
                .build();
    }

    @Override
    @Transactional
    public RefundResponse createRefund(CreateRefundRequest request, Long customerId) {
        // 1. Lấy snapshot booking + kiểm tra ownership (ném Forbidden nếu sai chủ).
        RefundableBookingContract booking =
                bookingRefundPort.loadRefundableBooking(request.getBookingId(), customerId);

        // 2. Eligibility.
        if (!Boolean.TRUE.equals(booking.getIsDepositPaid())) {
            throw new BusinessException(ErrorCode.REFUND_NOT_ELIGIBLE);
        }
        if (!BookingStatus.CONFIRMED.name().equals(booking.getStatus())) {
            throw new BusinessException(ErrorCode.BOOKING_NOT_CANCELABLE);
        }
        if (minutesUntilAppointment(booking) < CANCEL_THRESHOLD_MINUTES) {
            throw new BusinessException(ErrorCode.BOOKING_CANCEL_WINDOW_PASSED);
        }
        if (refundRepository.existsByBookingId(request.getBookingId())) {
            throw new BusinessException(ErrorCode.REFUND_ALREADY_EXISTS);
        }

        Refund saved = request.getRefundMethod() == RefundMethod.LOYALTY_POINTS
                ? createLoyaltyPointsRefund(request, customerId, booking)
                : createBankTransferRefund(request, booking);

        return refundMapper.toResponse(saved);
    }

    /** Hoàn cọc kiểu cũ: khách chọn ngân hàng, Admin xác nhận chuyển khoản thủ công (US-05). */
    private Refund createBankTransferRefund(CreateRefundRequest request, RefundableBookingContract booking) {
        if (request.getBankBin() == null || request.getBankBin().isBlank()) {
            throw new BusinessException(ErrorCode.REFUND_BANK_REQUIRED);
        }
        if (request.getAccountNumber() == null || request.getAccountNumber().isBlank()) {
            throw new BusinessException(ErrorCode.REFUND_ACCOUNT_NUMBER_REQUIRED);
        }
        if (request.getAccountHolder() == null || request.getAccountHolder().isBlank()) {
            throw new BusinessException(ErrorCode.REFUND_ACCOUNT_HOLDER_REQUIRED);
        }
        BankEnum bank = BankEnum.fromBin(request.getBankBin())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_BANK));

        Refund refund = Refund.builder()
                .booking(com.swp.autocarwash.booking.entity.Booking.builder()
                        .id(request.getBookingId()).build())
                .refundMethod(RefundMethod.BANK_TRANSFER.name())
                .refundBankName(bank.getDisplayName())
                .refundBankBin(bank.getBin())
                .refundAccountNumber(request.getAccountNumber())
                .refundAccountHolder(request.getAccountHolder())
                .refundAmount(booking.getDepositAmount())
                .status(RefundStatus.PENDING.name())
                .build();
        Refund saved = refundRepository.save(refund);

        // Chuyển booking sang REFUND_PENDING + giải phóng slot — chờ Admin duyệt.
        bookingRefundPort.markRefundPending(request.getBookingId());
        return saved;
    }

    /** Hoàn cọc bằng cách quy đổi thành điểm tích lũy — cộng điểm ngay, không cần Admin duyệt. */
    private Refund createLoyaltyPointsRefund(CreateRefundRequest request, Long customerId,
                                              RefundableBookingContract booking) {
        int points = loyaltyService.earnPointsFromDepositRefund(
                customerId, request.getBookingId(), booking.getDepositAmount());

        Refund refund = Refund.builder()
                .booking(com.swp.autocarwash.booking.entity.Booking.builder()
                        .id(request.getBookingId()).build())
                .refundMethod(RefundMethod.LOYALTY_POINTS.name())
                .pointsAwarded(points)
                .refundAmount(booking.getDepositAmount())
                .status(RefundStatus.REFUNDED.name())
                .refundedAt(Instant.now())
                .build();
        Refund saved = refundRepository.save(refund);

        // Đã hoàn xong ngay lập tức — chuyển thẳng booking sang REFUNDED.
        bookingRefundPort.markRefunded(request.getBookingId());
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public PointsPreviewResponse previewPointsConversion(Long bookingId, Long customerId) {
        RefundableBookingContract booking = bookingRefundPort.loadRefundableBooking(bookingId, customerId);

        if (!Boolean.TRUE.equals(booking.getIsDepositPaid())) {
            throw new BusinessException(ErrorCode.REFUND_NOT_ELIGIBLE);
        }

        PointsConversionPreview preview =
                loyaltyService.previewPointsForAmount(customerId, booking.getDepositAmount());

        return PointsPreviewResponse.builder()
                .bookingId(bookingId)
                .depositAmount(booking.getDepositAmount())
                .tierName(preview.getTierName())
                .pointMultiple(preview.getPointMultiple())
                .previewPoints(preview.getPreviewPoints())
                .build();
    }

    @Override
    public DepositAmountResponse getDepositAmount() {
        return DepositAmountResponse.builder()
                .amount(systemSettingService.getDepositAmount(
                        com.swp.autocarwash.system.service.impl.SystemSettingServiceImpl.DEFAULT_DEPOSIT_AMOUNT))
                .build();
    }

    /** Số phút từ hiện tại đến giờ hẹn (dùng slot start time, fallback 00:00). */
    private long minutesUntilAppointment(RefundableBookingContract booking) {
        LocalTime start = Optional.ofNullable(booking.getSlotStartTime()).orElse(LocalTime.MIDNIGHT);
        LocalDateTime appointment = LocalDateTime.of(booking.getAppointmentDate(), start);
        return ChronoUnit.MINUTES.between(LocalDateTime.now(ZONE), appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public RefundListPageResponse listRefundsForAdmin(String status, Integer year, Integer month,
                                                        Integer stationId, String keyword, Pageable pageable) {
        if (status != null) {
            try {
                RefundStatus.valueOf(status);
            } catch (IllegalArgumentException e) {
                throw new BusinessException(ErrorCode.INVALID_REQUEST);
            }
        }
        String normalizedKeyword = keyword != null && !keyword.isBlank() ? keyword.trim() : null;

        Page<Refund> page = refundRepository.findRefundsForAdmin(
                status, year, month, stationId, normalizedKeyword, pageable);

        Object[] summaryRow = refundRepository.getAdminSummary(
                status, year, month, stationId, normalizedKeyword).get(0);
        RefundSummaryResponse summary = RefundSummaryResponse.builder()
                .totalCount((Long) summaryRow[0])
                .totalRefundedAmount((BigDecimal) summaryRow[1])
                .build();

        return RefundListPageResponse.builder()
                .summary(summary)
                .content(page.getContent().stream().map(refundMapper::toListItemResponse).toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public RefundDetailResponse getRefundDetail(Long refundId) {
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.REFUND_NOT_FOUND));

        String qrImageUrl = refund.getRefundedAt() == null ? buildQrImageUrl(refund) : null;
        return refundMapper.toDetailResponse(refund, qrImageUrl);
    }

    @Override
    @Transactional
    public RefundDetailResponse confirmRefund(Long refundId, String transactionCode, Long adminId) {
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.REFUND_NOT_FOUND));

        if (refund.getRefundedAt() != null) {
            throw new BusinessException(ErrorCode.REFUND_ALREADY_PROCESSED);
        }

        refund.setRefundNote(transactionCode);
        refund.setRefundedAt(Instant.now());
        refund.setRefundedBy(adminId);
        refund.setStatus(RefundStatus.REFUNDED.name());
        Refund saved = refundRepository.save(refund);

        bookingRefundPort.markRefunded(saved.getBooking().getId());

        return refundMapper.toDetailResponse(saved, null);
    }

    /**
     * Build URL ảnh QR VietQR (AC2b) — chỉ khi bank map được qua BankEnum (AC2c), ngược lại trả null.
     */
    private String buildQrImageUrl(Refund refund) {
        Optional<BankEnum> bank = BankEnum.fromBin(refund.getRefundBankBin());
        if (bank.isEmpty()) {
            return null;
        }

        String template = systemSettingService.getStringValue(REFUND_TRANSFER_CONTENT_TEMPLATE);
        String addInfo = template.replace("{booking_id}", String.valueOf(refund.getBooking().getId()));

        return "https://img.vietqr.io/image/%s-%s-compact2.png?amount=%s&addInfo=%s&accountName=%s".formatted(
                bank.get().getBin(),
                refund.getRefundAccountNumber(),
                refund.getRefundAmount().toBigInteger().toString(),
                URLEncoder.encode(addInfo, StandardCharsets.UTF_8),
                URLEncoder.encode(refund.getRefundAccountHolder(), StandardCharsets.UTF_8));
    }
}
