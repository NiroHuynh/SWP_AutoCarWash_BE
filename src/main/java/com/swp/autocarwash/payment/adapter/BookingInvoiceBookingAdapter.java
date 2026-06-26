package com.swp.autocarwash.payment.adapter;

import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.booking.port.BookingInvoicePort;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.payment.entity.BookingInvoice;
import com.swp.autocarwash.payment.entity.enums.BookingInvoiceStatus;
import com.swp.autocarwash.payment.repository.BookingInvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class BookingInvoiceBookingAdapter implements BookingInvoicePort {

    private final BookingInvoiceRepository bookingInvoiceRepository;

    @Override
    public void createInvoice(Booking booking) {
//        kiểm tra xem booking này đã có invoice chưa
        if(bookingInvoiceRepository.existsByBookingId(booking.getId())){
            throw new BusinessException(ErrorCode.BOOKING_INVOICE_ALREADY_EXISTS);
        }

        BigDecimal rowAmount = booking.getTotalAddonAmount().add(booking.getTotalServiceAmount());

        BookingInvoice invoice = BookingInvoice.builder()
                .booking(booking)
                .customer(booking.getCustomer())
                .rawAmount(rowAmount)
                .serviceAmount(booking.getTotalServiceAmount())
                .addonAmount(booking.getTotalAddonAmount())
                .voucherDiscount(booking.getVoucherDiscountAmount())
                .discountAmount(booking.getVoucherDiscountAmount())
                .finalAmount(booking.getTotalAmount())
                .status(BookingInvoiceStatus.PROVISIONAL.toString())
                .build();

        bookingInvoiceRepository.save(invoice);
    }
}
