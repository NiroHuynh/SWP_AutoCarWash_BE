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
import com.swp.autocarwash.payment.dto.response.RedeemResult;
import com.swp.autocarwash.payment.entity.BookingInvoice;
import com.swp.autocarwash.payment.entity.Payment;
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

        if (booking.getCustomer() == null) {
            throw new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND);
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
        payment.setPaymentMethod("CASH");
        payment.setPaymentType("FULL_PAYMENT");
        payment.setAmount(amountDue);
        payment.setReceivedAmount(receivedAmount);
        payment.setPaymentStatus("SUCCESS");
        payment.setPaidAt(LocalDateTime.now());
        payment = paymentRepository.save(payment);

        // Bước 4: Khách đã thanh toán xong -> chuyển booking sang CHECK_OUT
        booking.setStatus(BookingStatus.CHECK_OUT.name());
        booking.setCheckOutAt(LocalDateTime.now());
        booking.setPointDiscountAmount(redeem.getRedeemAmount());
        bookingRepository.save(booking);

        updateAndCreatePointBalanceAndTransaction(booking.getCustomer(),booking,redeem.getUsedPoints());
        createBookingCompletedEvent(booking);

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
}
