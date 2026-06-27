package com.swp.autocarwash.payment.repository;

import com.swp.autocarwash.payment.entity.BookingInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingInvoiceRepository extends JpaRepository<BookingInvoice, Long> {

    boolean existsByBookingId(Long bookingId);

    Optional<BookingInvoice> findByBookingId(Long bookingId);

    Optional<BookingInvoice> findByBooking_Id(Long bookingId);
}
