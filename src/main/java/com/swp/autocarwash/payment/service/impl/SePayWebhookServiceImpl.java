package com.swp.autocarwash.payment.service.impl;

import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.booking.entity.enums.BookingStatus;
import com.swp.autocarwash.booking.event.BookingDepositPaidEvent;
import com.swp.autocarwash.booking.repository.BookingRepository;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.ResourceNotFoundException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.payment.dto.request.ManualDepositConfirmRequest;
import com.swp.autocarwash.payment.dto.request.ManualSubscriptionConfirmRequest;
import com.swp.autocarwash.payment.dto.request.SePayWebhookRequest;
import com.swp.autocarwash.payment.dto.response.SubscriptionPaymentInitResponse;
import com.swp.autocarwash.payment.dto.response.DepositConfirmResponse;
import com.swp.autocarwash.payment.dto.response.WebhookLogResponse;
import com.swp.autocarwash.payment.entity.Payment;
import com.swp.autocarwash.payment.entity.PaymentWebhookLog;
import com.swp.autocarwash.payment.entity.SubscriptionInvoice;
import com.swp.autocarwash.payment.entity.enums.PaymentMethod;
import com.swp.autocarwash.payment.entity.enums.PaymentStatus;
import com.swp.autocarwash.payment.entity.enums.PaymentType;
import com.swp.autocarwash.payment.entity.enums.WebhookLogStatus;
import com.swp.autocarwash.payment.event.PaymentEventPublisher;
import com.swp.autocarwash.payment.event.SubscriptionInvoicePaidEvent;
import com.swp.autocarwash.payment.repository.PaymentRepository;
import com.swp.autocarwash.payment.repository.PaymentWebhookLogRepository;
import com.swp.autocarwash.payment.repository.SubscriptionInvoiceRepository;
import com.swp.autocarwash.payment.service.SePayWebhookService;
import com.swp.autocarwash.system.service.impl.SystemSettingServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Triển khai nghiệp vụ xác nhận cọc qua webhook SePay định nghĩa trong
 * {@link SePayWebhookService}.
 *
 * @author Ngân
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SePayWebhookServiceImpl implements SePayWebhookService {

    /** Nội dung chuyển khoản dạng "BK123" — nhóm 1 là booking id. */
    private static final Pattern BOOKING_CODE_PATTERN = Pattern.compile("BK\\s*(\\d+)", Pattern.CASE_INSENSITIVE);

    /** Nội dung chuyển khoản mua gói dạng "SUB123" — nhóm 1 là subscription invoice id. */
    private static final Pattern SUBSCRIPTION_CODE_PATTERN = Pattern.compile("SUB\\s*(\\d+)", Pattern.CASE_INSENSITIVE);

    private static final String TRANSFER_IN = "in";

    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentWebhookLogRepository webhookLogRepository;
    private final SubscriptionInvoiceRepository subscriptionInvoiceRepository;
    private final PaymentEventPublisher paymentEventPublisher;
    private final SystemSettingServiceImpl systemSettingService;

    /**
     * {@inheritDoc}
     *
     * <p>Luồng xử lý:
     * <ol>
     *   <li>Idempotent: mã giao dịch SePay đã có trong log → bỏ qua.</li>
     *   <li>Chỉ xử lý giao dịch tiền vào (transferType = "in").</li>
     *   <li>Regex "BK{id}" trên nội dung CK → tìm booking; không thấy → log ORPHAN.</li>
     *   <li>Đối chiếu số tiền với tiền cọc hệ thống; lệch → log AMOUNT_MISMATCH.</li>
     *   <li>Booking phải PENDING và chưa đóng cọc; sai → log INVALID_BOOKING_STATE.</li>
     *   <li>Hợp lệ → lưu Payment, set isDepositPaid, PENDING → CONFIRMED, phát event.</li>
     * </ol>
     * </p>
     */
    @Override
    @Transactional
    public void handleWebhook(SePayWebhookRequest request) {
        String transactionId = String.valueOf(request.getId());

        // Bước 1: idempotent — SePay có thể gửi lại cùng một giao dịch nhiều lần
        if (webhookLogRepository.existsBySepayTransactionId(transactionId)) {
            log.info("SePay webhook {} đã xử lý trước đó, bỏ qua", transactionId);
            return;
        }

        // Bước 2: chỉ quan tâm tiền vào
        if (!TRANSFER_IN.equalsIgnoreCase(request.getTransferType())) {
            log.info("SePay webhook {} là giao dịch tiền ra, bỏ qua", transactionId);
            return;
        }

        // Nhánh mua gói: nội dung CK "SUB{invoiceId}" → xử lý riêng
        if (request.getContent() != null) {
            Matcher subMatcher = SUBSCRIPTION_CODE_PATTERN.matcher(request.getContent());
            if (subMatcher.find()) {
                handleSubscriptionPayment(request, subMatcher.group(1));
                return;
            }
        }

        // Bước 3: map booking từ nội dung chuyển khoản
        Booking booking = extractBooking(request.getContent());
        if (booking == null) {
            saveLog(request, null, WebhookLogStatus.ORPHAN,
                    "Không tìm thấy booking khớp nội dung chuyển khoản");
            log.warn("SePay webhook {} orphan, content='{}'", transactionId, request.getContent());
            return;
        }

        // Bước 4: đối chiếu số tiền với tiền cọc yêu cầu
        BigDecimal depositAmount = systemSettingService
                .getDepositAmount(SystemSettingServiceImpl.DEFAULT_DEPOSIT_AMOUNT);
        if (request.getTransferAmount() == null
                || request.getTransferAmount().compareTo(depositAmount) != 0) {
            saveLog(request, booking, WebhookLogStatus.AMOUNT_MISMATCH,
                    "Số tiền không khớp tiền cọc " + depositAmount);
            log.warn("SePay webhook {} sai số tiền: nhận {}, cần {}",
                    transactionId, request.getTransferAmount(), depositAmount);
            return;
        }

        // Bước 5: kiểm tra trạng thái booking trước khi update
        if (!BookingStatus.PENDING.name().equals(booking.getStatus())
                || Boolean.TRUE.equals(booking.getIsDepositPaid())) {
            saveLog(request, booking, WebhookLogStatus.INVALID_BOOKING_STATE,
                    "Booking đang ở trạng thái " + booking.getStatus()
                            + ", isDepositPaid=" + booking.getIsDepositPaid());
            return;
        }

        // Bước 6: xác nhận cọc
        saveLog(request, booking, WebhookLogStatus.PROCESSED, null);
        confirmDeposit(booking, depositAmount, PaymentMethod.BANK_TRANSFER, transactionId);
        log.info("SePay webhook {}: xác nhận cọc booking {} thành công", transactionId, booking.getId());
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public DepositConfirmResponse manualConfirmDeposit(ManualDepositConfirmRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BOOKING_NOT_FOUND));

        // Check trạng thái trước khi update để tránh xung đột với webhook tự động
        if (!BookingStatus.PENDING.name().equals(booking.getStatus())
                || Boolean.TRUE.equals(booking.getIsDepositPaid())) {
            throw new BusinessException(ErrorCode.DEPOSIT_ALREADY_CONFIRMED);
        }

        BigDecimal depositAmount = systemSettingService
                .getDepositAmount(SystemSettingServiceImpl.DEFAULT_DEPOSIT_AMOUNT);

        confirmDeposit(booking, depositAmount, PaymentMethod.MANUAL,
                request.getNote() != null ? request.getNote() : "MANUAL-" + booking.getId());

        return DepositConfirmResponse.builder()
                .bookingId(booking.getId())
                .bookingStatus(booking.getStatus())
                .isDepositPaid(booking.getIsDepositPaid())
                .depositAmount(depositAmount)
                .build();
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public SubscriptionPaymentInitResponse manualConfirmSubscription(ManualSubscriptionConfirmRequest request) {
        SubscriptionInvoice invoice = subscriptionInvoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.SUBSCRIPTION_INVOICE_NOT_FOUND));

        // Check trạng thái trước khi update để tránh xung đột với webhook tự động
        if (!SubscriptionPaymentServiceImpl.INVOICE_PENDING.equals(invoice.getStatus())) {
            throw new BusinessException(ErrorCode.INVOICE_NOT_PENDING);
        }

        confirmSubscriptionPayment(invoice, PaymentMethod.MANUAL,
                request.getNote() != null ? request.getNote() : "MANUAL-" + invoice.getId());

        return SubscriptionPaymentInitResponse.builder()
                .invoiceId(invoice.getId())
                .amount(invoice.getPlanPrice())
                .invoiceStatus(invoice.getStatus())
                .build();
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public List<WebhookLogResponse> getWebhookLogs(String status) {
        List<PaymentWebhookLog> logs = (status == null || status.isBlank())
                ? webhookLogRepository.findAll()
                : webhookLogRepository.findByStatusOrderByReceivedAtDesc(status.toUpperCase());

        return logs.stream()
                .map(l -> WebhookLogResponse.builder()
                        .id(l.getId())
                        .sepayTransactionId(l.getSepayTransactionId())
                        .gateway(l.getGateway())
                        .transferAmount(l.getTransferAmount())
                        .transferContent(l.getTransferContent())
                        .transactionDate(l.getTransactionDate())
                        .bookingId(l.getBooking() != null ? l.getBooking().getId() : null)
                        .subscriptionInvoiceId(l.getSubscriptionInvoice() != null
                                ? l.getSubscriptionInvoice().getId() : null)
                        .status(l.getStatus())
                        .note(l.getNote())
                        .receivedAt(l.getReceivedAt())
                        .build())
                .toList();
    }

    /**
     * Logic xác nhận cọc dùng chung cho webhook và manual confirm: lưu Payment,
     * set isDepositPaid, chuyển booking PENDING → CONFIRMED, phát event.
     */
    private void confirmDeposit(Booking booking, BigDecimal depositAmount,
                                PaymentMethod paymentMethod, String transactionCode) {
        Payment payment = new Payment();
        payment.setPaymentMethod(paymentMethod.name());
        payment.setPaymentType(PaymentType.DEPOSIT.name());
        payment.setAmount(depositAmount);
        payment.setReceivedAmount(depositAmount);
        payment.setTransactionCode(transactionCode);
        payment.setPaymentStatus(PaymentStatus.SUCCESS.name());
        payment.setPaidAt(LocalDateTime.now());
        paymentRepository.save(payment);

        booking.setIsDepositPaid(true);
        booking.setStatus(BookingStatus.CONFIRMED.name());
        bookingRepository.save(booking);

        Long customerId = booking.getCustomer() != null ? booking.getCustomer().getId() : null;
        paymentEventPublisher.publishBookingDepositPaid(
                new BookingDepositPaidEvent(booking.getId(), customerId, depositAmount, paymentMethod.name()));
    }

    /**
     * Nhánh webhook mua gói: tìm SubscriptionInvoice từ nội dung CK "SUB{id}",
     * đối chiếu trạng thái + số tiền với planPrice, xác nhận PAID và bắn event
     * cho module subscription kích hoạt gói. Case lỗi được log lại, không throw.
     */
    private void handleSubscriptionPayment(SePayWebhookRequest request, String invoiceIdRaw) {
        String transactionId = String.valueOf(request.getId());

        SubscriptionInvoice invoice;
        try {
            invoice = subscriptionInvoiceRepository.findById(Long.parseLong(invoiceIdRaw)).orElse(null);
        } catch (NumberFormatException e) {
            invoice = null;
        }
        if (invoice == null) {
            saveSubscriptionLog(request, null, WebhookLogStatus.ORPHAN,
                    "Không tìm thấy hóa đơn mua gói khớp nội dung chuyển khoản");
            log.warn("SePay webhook {} orphan (SUB), content='{}'", transactionId, request.getContent());
            return;
        }

        if (!SubscriptionPaymentServiceImpl.INVOICE_PENDING.equals(invoice.getStatus())) {
            saveSubscriptionLog(request, invoice, WebhookLogStatus.INVALID_INVOICE_STATE,
                    "Hóa đơn đang ở trạng thái " + invoice.getStatus());
            return;
        }

        if (request.getTransferAmount() == null
                || request.getTransferAmount().compareTo(invoice.getPlanPrice()) != 0) {
            saveSubscriptionLog(request, invoice, WebhookLogStatus.AMOUNT_MISMATCH,
                    "Số tiền không khớp giá gói " + invoice.getPlanPrice());
            log.warn("SePay webhook {} sai số tiền gói: nhận {}, cần {}",
                    transactionId, request.getTransferAmount(), invoice.getPlanPrice());
            return;
        }

        saveSubscriptionLog(request, invoice, WebhookLogStatus.PROCESSED, null);
        confirmSubscriptionPayment(invoice, PaymentMethod.BANK_TRANSFER, transactionId);
        log.info("SePay webhook {}: thanh toán hóa đơn mua gói {} thành công", transactionId, invoice.getId());
    }

    /**
     * Logic xác nhận thanh toán mua gói dùng chung cho webhook và manual confirm:
     * set invoice PAID, lưu Payment, bắn event cho module subscription kích hoạt gói.
     */
    private void confirmSubscriptionPayment(SubscriptionInvoice invoice,
                                            PaymentMethod paymentMethod, String transactionCode) {
        invoice.setStatus(SubscriptionPaymentServiceImpl.INVOICE_PAID);
        invoice.setPaidAt(Instant.now());
        subscriptionInvoiceRepository.save(invoice);

        Payment payment = new Payment();
        payment.setSubscriptionInvoice(invoice);
        payment.setPaymentMethod(paymentMethod.name());
        payment.setPaymentType(PaymentType.SUBSCRIPTION.name());
        payment.setAmount(invoice.getPlanPrice());
        payment.setReceivedAmount(invoice.getPlanPrice());
        payment.setTransactionCode(transactionCode);
        payment.setPaymentStatus(PaymentStatus.SUCCESS.name());
        payment.setPaidAt(LocalDateTime.now());
        paymentRepository.save(payment);

        paymentEventPublisher.publishSubscriptionInvoicePaid(new SubscriptionInvoicePaidEvent(
                invoice.getId(),
                invoice.getCustomer().getId(),
                invoice.getPlanPrice(),
                invoice.getUnlimitSubscription() != null ? invoice.getUnlimitSubscription().getId() : null,
                invoice.getFamilySubscription() != null ? invoice.getFamilySubscription().getId() : null,
                paymentMethod.name()));
    }

    private void saveSubscriptionLog(SePayWebhookRequest request, SubscriptionInvoice invoice,
                                     WebhookLogStatus status, String note) {
        PaymentWebhookLog webhookLog = PaymentWebhookLog.builder()
                .sepayTransactionId(String.valueOf(request.getId()))
                .gateway(request.getGateway())
                .transferAmount(request.getTransferAmount())
                .transferContent(request.getContent())
                .transactionDate(request.getTransactionDate())
                .subscriptionInvoice(invoice)
                .status(status.name())
                .note(note)
                .receivedAt(LocalDateTime.now())
                .build();
        webhookLogRepository.save(webhookLog);
    }

    /** Tách booking id từ nội dung chuyển khoản theo pattern "BK{id}". */
    private Booking extractBooking(String content) {
        if (content == null) {
            return null;
        }
        Matcher matcher = BOOKING_CODE_PATTERN.matcher(content);
        if (!matcher.find()) {
            return null;
        }
        try {
            Long bookingId = Long.parseLong(matcher.group(1));
            return bookingRepository.findById(bookingId).orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void saveLog(SePayWebhookRequest request, Booking booking,
                         WebhookLogStatus status, String note) {
        PaymentWebhookLog webhookLog = PaymentWebhookLog.builder()
                .sepayTransactionId(String.valueOf(request.getId()))
                .gateway(request.getGateway())
                .transferAmount(request.getTransferAmount())
                .transferContent(request.getContent())
                .transactionDate(request.getTransactionDate())
                .booking(booking)
                .status(status.name())
                .note(note)
                .receivedAt(LocalDateTime.now())
                .build();
        webhookLogRepository.save(webhookLog);
    }
}
