package com.swp.autocarwash.payment.service.impl;

import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.booking.entity.enums.BookingStatus;
import com.swp.autocarwash.booking.event.BookingCompletedEvent;
import com.swp.autocarwash.booking.event.BookingEventPublisher;
import com.swp.autocarwash.booking.repository.BookingRepository;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.ResourceNotFoundException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.payment.dto.request.CashPaymentRequest;
import com.swp.autocarwash.payment.dto.response.CashPaymentResponse;
import com.swp.autocarwash.payment.entity.BookingInvoice;
import com.swp.autocarwash.payment.entity.Payment;
import com.swp.autocarwash.payment.repository.BookingInvoiceRepository;
import com.swp.autocarwash.payment.repository.PaymentRepository;
import com.swp.autocarwash.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

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
    private final BookingEventPublisher bookingEventPublisher;

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

        // Bước 1: Tính số tiền còn phải thu = tổng tiền - tiền cọc đã trả (nếu có)
        BigDecimal deposit = Boolean.TRUE.equals(booking.getIsDepositPaid())
                ? DEFAULT_DEPOSIT_AMOUNT
                : BigDecimal.ZERO;
        BigDecimal amountDue = booking.getTotalAmount().subtract(deposit);

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
        bookingRepository.save(booking);

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
}
