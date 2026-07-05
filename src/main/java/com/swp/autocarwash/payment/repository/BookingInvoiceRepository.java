package com.swp.autocarwash.payment.repository;

import com.swp.autocarwash.payment.entity.BookingInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface BookingInvoiceRepository extends JpaRepository<BookingInvoice, Long> {

    boolean existsByBookingId(Long bookingId);

    Optional<BookingInvoice> findByBookingId(Long bookingId);

    Optional<BookingInvoice> findByBooking_Id(Long bookingId);

    /**
     * Tong tien khach da thanh toan qua hoa don booking trong khoang [from, to).
     * Loc theo paidAt (hoa don chua thanh toan co paidAt = NULL nen tu loai).
     */
    @Query("SELECT COALESCE(SUM(bi.finalAmount), 0) FROM BookingInvoice bi " +
           "WHERE bi.customer.id = :cid AND bi.paidAt >= :from AND bi.paidAt < :to")
    BigDecimal sumFinalAmountPaidBetween(@Param("cid") Long cid,
                                         @Param("from") LocalDateTime from,
                                         @Param("to") LocalDateTime to);
}
