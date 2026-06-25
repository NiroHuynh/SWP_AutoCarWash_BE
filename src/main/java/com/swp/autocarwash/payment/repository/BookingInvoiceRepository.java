package com.swp.autocarwash.payment.repository;

import com.swp.autocarwash.payment.entity.BookingInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository truy vấn hoá đơn booking ({@link BookingInvoice}).
 *
 * @author Ngọc
 * @version 1.0
 */
@Repository
public interface BookingInvoiceRepository extends JpaRepository<BookingInvoice, Long> {

    /**
     * Tìm hoá đơn theo booking — mỗi booking chỉ có tối đa 1 hoá đơn (quan hệ OneToOne).
     *
     * @param bookingId mã định danh booking
     * @return {@link Optional} chứa {@link BookingInvoice} nếu đã tồn tại
     */
    Optional<BookingInvoice> findByBooking_Id(Long bookingId);
}
