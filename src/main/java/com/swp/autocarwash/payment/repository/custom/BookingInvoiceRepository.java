package com.swp.autocarwash.payment.repository.custom;

import com.swp.autocarwash.payment.entity.BookingInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingInvoiceRepository extends JpaRepository<BookingInvoice,Long> {
    Optional<BookingInvoice> findByBooking_Id(Long bookingId);
    //Optional<BookingInvoice> findByBookingId
}
