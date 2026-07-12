package com.swp.autocarwash.payment.service.impl;

import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.booking.entity.enums.BookingStatus;
import com.swp.autocarwash.booking.event.BookingCompletedEvent;
import com.swp.autocarwash.booking.event.BookingEventPublisher;
import com.swp.autocarwash.booking.repository.BookingRepository;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.ResourceNotFoundException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.loyalty.entity.LoyaltyPointBalance;
import com.swp.autocarwash.loyalty.entity.LoyaltyPointTransaction;
import com.swp.autocarwash.loyalty.entity.enums.LoyaltyPointTransactionType;
import com.swp.autocarwash.loyalty.entity.enums.LoyaltySourceType;
import com.swp.autocarwash.loyalty.repository.LoyaltyPointBalanceRepository;
import com.swp.autocarwash.loyalty.repository.LoyaltyPointTransactionRepository;
import com.swp.autocarwash.payment.dto.request.CashPaymentRequest;
import com.swp.autocarwash.payment.dto.response.CashPaymentResponse;
import com.swp.autocarwash.payment.dto.response.InvoiceDetailResponse;
import com.swp.autocarwash.payment.dto.response.PaymentHistoryResponse;
import com.swp.autocarwash.payment.dto.response.PaymentTransactionHistoryResponse;
import com.swp.autocarwash.payment.dto.response.PaymentTransactionResponse;
import com.swp.autocarwash.payment.dto.response.PaymentTransactionSummaryResponse;
import com.swp.autocarwash.payment.dto.response.RedeemResult;
import com.swp.autocarwash.payment.entity.BookingInvoice;
import com.swp.autocarwash.payment.entity.Payment;
import com.swp.autocarwash.payment.entity.enums.PaymentMethod;
import com.swp.autocarwash.payment.entity.enums.PaymentStatus;
import com.swp.autocarwash.payment.entity.enums.PaymentType;
import com.swp.autocarwash.payment.repository.BookingInvoiceRepository;
import com.swp.autocarwash.payment.repository.PaymentRepository;
import com.swp.autocarwash.payment.service.PaymentService;
import com.swp.autocarwash.system.service.impl.SystemSettingServiceImpl;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

/**
 * Triển khai nghiệp vụ thanh toán tiền mặt định nghĩa trong {@link PaymentService}.
 *
 * @author Ngọc
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    /**
     * Số tiền đặt cọc cố định theo BL-BK-00 (khớp với BookingServiceImpl) —
     * dùng để trừ vào totalAmount khi tính số tiền còn phải thu tại quầy.
     */
    private static final BigDecimal DEFAULT_DEPOSIT_AMOUNT = BigDecimal.valueOf(20000);

    private final BookingRepository bookingRepository;
    private final BookingInvoiceRepository bookingInvoiceRepository;
    private final PaymentRepository paymentRepository;
    private final LoyaltyPointBalanceRepository loyaltyPointBalanceRepository;
    private final LoyaltyPointTransactionRepository loyaltyPointTransactionRepository;
    private final BookingEventPublisher bookingEventPublisher;
    private final SystemSettingServiceImpl systemSettingService;

    /**
     * {@inheritDoc}
     *
     * <p>Luồng xử lý:
     * <ol>
     *   <li>Tìm booking theo ID (404 nếu không tồn tại).</li>
     *   <li>Chỉ cho thu tiền khi booking đang ở trạng thái COMPLETED.</li>
     *   <li>Tính số tiền còn phải thu = totalAmount - tiền cọc (nếu isDepositPaid).</li>
     *   <li>Validate receivedAmount &gt;= số tiền còn phải thu, nếu không đủ thì báo lỗi.</li>
     *   <li>Tạo/cập nhật {@link BookingInvoice} với status = PAID.</li>
     *   <li>Lưu {@link Payment} với paymentMethod = CASH, paymentStatus = SUCCESS.</li>
     *   <li>Chuyển booking sang CHECK_OUT, set checkOutAt = now.</li>
     * </ol>
     * </p>
     */
    @Override
    @Transactional
    public CashPaymentResponse processCashPayment(CashPaymentRequest request) {
        Booking booking = bookingRepository.findDetailById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BOOKING_NOT_FOUND));

        if (!"COMPLETED".equals(booking.getStatus())) {
            throw new BusinessException(ErrorCode.BOOKING_NOT_COMPLETED);
        }

        // Bước 1: Tính số tiền còn phải thu = tổng tiền - tiền cọc đã trả - điểm sử dụng (nếu có)
        BigDecimal deposit = Boolean.TRUE.equals(booking.getIsDepositPaid())
                ? DEFAULT_DEPOSIT_AMOUNT
                : BigDecimal.ZERO;
        BigDecimal amountBeforeRedeem = booking.getTotalAmount().subtract(deposit);
        RedeemResult redeem = calculateRedeemAmount(
                booking.getCustomer(),
                request.getUsedLoyaltyPoints(),
                amountBeforeRedeem
        );

        BigDecimal amountDue = amountBeforeRedeem.subtract(redeem.getRedeemAmount());

        BigDecimal receivedAmount = request.getReceivedAmount();
        if (receivedAmount.compareTo(amountDue) < 0) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_PAYMENT_AMOUNT);
        }
        BigDecimal changeAmount = receivedAmount.subtract(amountDue);

        // Bước 2: Tạo hoặc cập nhật hoá đơn (BookingInvoice) cho booking này —
        // mỗi booking chỉ có tối đa 1 invoice (quan hệ OneToOne)
        BookingInvoice invoice = bookingInvoiceRepository.findByBooking_Id(booking.getId())
                .orElseGet(BookingInvoice::new);

        BigDecimal serviceAmount = nvl(booking.getTotalServiceAmount());
        BigDecimal addonAmount = nvl(booking.getTotalAddonAmount());
        BigDecimal voucherDiscount = nvl(booking.getVoucherDiscountAmount());
        BigDecimal pointDiscount = nvl(booking.getPointDiscountAmount());

        invoice.setBooking(booking);
        invoice.setCustomer(booking.getCustomer());
        invoice.setServiceAmount(serviceAmount);
        invoice.setAddonAmount(addonAmount);
        invoice.setVoucherDiscount(voucherDiscount);
        invoice.setPointDiscount(pointDiscount);
        invoice.setDiscountAmount(voucherDiscount.add(pointDiscount));
        invoice.setRawAmount(serviceAmount.add(addonAmount));
        invoice.setFinalAmount(booking.getTotalAmount());
        invoice.setStatus("PAID");
        invoice.setPaidAt(LocalDateTime.now());
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setPointDiscount(redeem.getRedeemAmount());
        invoice = bookingInvoiceRepository.save(invoice);

        // Bước 3: Ghi nhận giao dịch thanh toán tiền mặt, gắn vào hoá đơn vừa tạo/cập nhật
        Payment payment = new Payment();
        payment.setBookingInvoice(invoice);
        payment.setPaymentMethod(PaymentMethod.CASH.name());
        payment.setPaymentType(PaymentType.FULL_PAYMENT.name());
        payment.setAmount(amountDue);
        payment.setReceivedAmount(receivedAmount);
        payment.setPaymentStatus(PaymentStatus.SUCCESS.name());
        payment.setPaidAt(LocalDateTime.now());
        payment = paymentRepository.save(payment);

        // Bước 4: Khách đã thanh toán xong -> chuyển booking sang CHECK_OUT
        booking.setStatus(BookingStatus.CHECK_OUT.name());
        booking.setCheckOutAt(LocalDateTime.now());
        booking.setPointDiscountAmount(redeem.getRedeemAmount());
        bookingRepository.save(booking);

        updateAndCreatePointBalanceAndTransaction(booking.getCustomer(),booking,redeem.getUsedPoints());
        if (booking.getCustomer() != null) {
            createBookingCompletedEvent(booking);
        }

        return CashPaymentResponse.builder()
                .invoiceId(invoice.getId())
                .totalAmount(amountDue)
                .receivedAmount(receivedAmount)
                .changeAmount(changeAmount)
                .bookingStatus(booking.getStatus())
                .paymentStatus(payment.getPaymentStatus())
                .build();
    }

    private BigDecimal nvl(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    private void createBookingCompletedEvent(Booking booking){
        BookingCompletedEvent event =
                 new BookingCompletedEvent(
                        booking.getId(),
                        booking.getCustomer().getId(),
                        booking.getTotalAmount(),
                        booking.getCustomer().getCustomerTier().getId(),
                        booking.getCustomer().getCustomerTier().getPointMultiple()
                );
        bookingEventPublisher.publicBookingCompleted(event);
    }

    @Transactional(readOnly = true)
    public RedeemResult calculateRedeemAmount(
            Customer customer,
            BigDecimal usedPoints,
            BigDecimal amountBeforeRedeem
    ) {

        // Không sử dụng điểm
        if (usedPoints == null || usedPoints.compareTo(BigDecimal.ZERO) <= 0) {
            return RedeemResult.builder()
                    .usedPoints(BigDecimal.ZERO)
                    .redeemAmount(BigDecimal.ZERO)
                    .build();
        }

        LoyaltyPointBalance balance = loyaltyPointBalanceRepository
                .findLoyaltyPointBalanceByCustomerId(customer.getId())
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.LOYALTY_POINT_BALANCE_NOT_FOUND));

        // Không đủ điểm
        if (balance.getTotalPoints().compareTo(Integer.parseInt(usedPoints.toString())) < 0) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_LOYALTY_POINTS);
        }

        BigDecimal redeemRate = systemSettingService.getLoyaltyRedeemRate();

        BigDecimal redeemAmount = usedPoints.multiply(redeemRate);
        BigDecimal actualUsedPoints = usedPoints;

        // Nếu số tiền giảm lớn hơn số tiền cần thanh toán
        if (redeemAmount.compareTo(amountBeforeRedeem) > 0) {

            redeemAmount = amountBeforeRedeem;

            actualUsedPoints = amountBeforeRedeem.divide(
                    redeemRate,
                    0,
                    RoundingMode.DOWN
            );

            redeemAmount = actualUsedPoints.multiply(redeemRate);
        }

        return RedeemResult.builder()
                .usedPoints(actualUsedPoints)
                .redeemAmount(redeemAmount)
                .build();
    }



    @Transactional
    public void updateAndCreatePointBalanceAndTransaction(
            Customer customer,
            Booking booking,
            BigDecimal usedPoints
    ) {

        if (usedPoints == null || usedPoints.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        LoyaltyPointBalance balance = loyaltyPointBalanceRepository
                .findLoyaltyPointBalanceByCustomerId(customer.getId())
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.LOYALTY_POINT_BALANCE_NOT_FOUND));

        balance.setTotalPoints(
                balance.getTotalPoints() - usedPoints.intValue()
        );

        loyaltyPointBalanceRepository.save(balance);

        LoyaltyPointTransaction transaction =
                LoyaltyPointTransaction.builder()
                        .customer(customer)
                        .booking(booking)
                        .transactionType(LoyaltyPointTransactionType.REDEEM)
                        .points(-usedPoints.intValue())
                        .balanceAfter(balance.getTotalPoints())
                        .createdAt(LocalDateTime.now())
                        .sourceType(LoyaltySourceType.BOOKING)
                        .build();


        loyaltyPointTransactionRepository.save(transaction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<PaymentHistoryResponse> getPaymentHistory(
            Long customerId, String type, LocalDateTime fromDate, LocalDateTime toDate) {

        return paymentRepository
                .findSuccessfulPaymentsByCustomerId(customerId, type, fromDate, toDate)
                .stream()
                .map(this::toPaymentHistoryResponse)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public PaymentTransactionHistoryResponse getTransactionHistory(
            String method, String status, String type, LocalDateTime fromDate, LocalDateTime toDate,
            Long bookingId, Long transactionId, Integer stationId, String phone) {

        List<Payment> payments = paymentRepository.findAllTransactions(
                method, status, type, fromDate, toDate, bookingId, transactionId, stationId, phone);

        List<PaymentTransactionResponse> transactions = payments.stream()
                .map(this::toPaymentTransactionResponse)
                .toList();

        BigDecimal totalRevenue = payments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        PaymentTransactionSummaryResponse summary = PaymentTransactionSummaryResponse.builder()
                .totalRevenue(totalRevenue)
                .totalCount(payments.size())
                .build();

        return PaymentTransactionHistoryResponse.builder()
                .summary(summary)
                .transactions(transactions)
                .build();
    }

    private PaymentTransactionResponse toPaymentTransactionResponse(Payment payment) {
        Long bookingId = payment.getBookingInvoice() != null && payment.getBookingInvoice().getBooking() != null
                ? payment.getBookingInvoice().getBooking().getId()
                : null;

        return PaymentTransactionResponse.builder()
                .id(payment.getId())
                .bookingId(bookingId)
                .customerPhone(resolveCustomerPhone(payment))
                .paymentMethod(payment.getPaymentMethod())
                .amount(payment.getAmount())
                .paymentStatus(payment.getPaymentStatus())
                .paidAt(payment.getPaidAt())
                .build();
    }

    private String resolveCustomerPhone(Payment payment) {
        if (payment.getBookingInvoice() != null && payment.getBookingInvoice().getCustomer() != null) {
            return payment.getBookingInvoice().getCustomer().getUser().getPhone();
        }
        if (payment.getSubscriptionInvoice() != null && payment.getSubscriptionInvoice().getCustomer() != null) {
            return payment.getSubscriptionInvoice().getCustomer().getUser().getPhone();
        }
        return null;
    }

    private PaymentHistoryResponse toPaymentHistoryResponse(Payment payment) {
        Long bookingId = payment.getBookingInvoice() != null && payment.getBookingInvoice().getBooking() != null
                ? payment.getBookingInvoice().getBooking().getId()
                : null;
        Long subscriptionInvoiceId = payment.getSubscriptionInvoice() != null
                ? payment.getSubscriptionInvoice().getId()
                : null;

        return PaymentHistoryResponse.builder()
                .id(payment.getId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .paymentType(payment.getPaymentType())
                .transactionCode(payment.getTransactionCode())
                .paidAt(payment.getPaidAt())
                .bookingId(bookingId)
                .subscriptionInvoiceId(subscriptionInvoiceId)
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public InvoiceDetailResponse getInvoiceDetail(Long invoiceId) {
        BookingInvoice invoice = bookingInvoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.INVOICE_NOT_FOUND));

        Booking booking = invoice.getBooking();

        List<InvoiceDetailResponse.ServiceLine> services = new ArrayList<>();
        if (booking != null && booking.getServicePackage() != null) {
            services.add(InvoiceDetailResponse.ServiceLine.builder()
                    .name(booking.getServicePackage().getName())
                    .price(booking.getServicePackage().getBasePrice())
                    .build());
        }
        if (booking != null) {
            booking.getAddons().forEach(addon -> services.add(InvoiceDetailResponse.ServiceLine.builder()
                    .name(addon.getAddonService().getName())
                    .price(addon.getPrice())
                    .build()));
        }

        List<Payment> payments = paymentRepository.findByBookingInvoiceIdOrderByPaidAtDesc(invoiceId);
        String paymentMethod = payments.isEmpty() ? null : payments.get(0).getPaymentMethod();

        return InvoiceDetailResponse.builder()
                .invoiceId(invoice.getId())
                .invoiceStatus(invoice.getStatus())
                .paidAt(invoice.getPaidAt())
                .bookingId(booking != null ? booking.getId() : null)
                .vehicleBrand(booking != null ? booking.getVehicle().getBrandName() : null)
                .vehicleLicensePlate(booking != null ? booking.getVehicle().getLicensePlate() : null)
                .servicePackageName(booking != null ? booking.getServicePackage().getName() : null)
                .appointmentDate(booking != null ? booking.getAppointmentDate() : null)
                .checkInAt(booking != null ? booking.getCheckInAt() : null)
                .checkOutAt(booking != null ? booking.getCheckOutAt() : null)
                .services(services)
                .rawAmount(invoice.getRawAmount())
                .serviceAmount(invoice.getServiceAmount())
                .addonAmount(invoice.getAddonAmount())
                .voucherDiscount(invoice.getVoucherDiscount())
                .pointDiscount(invoice.getPointDiscount())
                .discountAmount(invoice.getDiscountAmount())
                .finalAmount(invoice.getFinalAmount())
                .paymentMethod(paymentMethod)
                .build();
    }
}
