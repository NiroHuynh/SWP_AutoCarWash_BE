package com.swp.autocarwash.promotion.repository;

import com.swp.autocarwash.promotion.entity.VoucherUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository truy vấn thông tin sử dụng voucher ({@link VoucherUsage}).
 *
 * @author KimNgan
 * @version 1.0
 */
@Repository
public interface VoucherUsageRepository extends JpaRepository<VoucherUsage, Long> {

    /**
     * Lấy thông tin voucher đã được áp dụng thành công cho một booking.
     *
     * @param bookingId mã định danh của lịch đặt
     * @return {@link Optional} chứa {@link VoucherUsage} đã eager-fetch {@code voucher},
     *         hoặc rỗng nếu booking không dùng voucher
     */
    @Query("SELECT vu FROM VoucherUsage vu " +
           "JOIN FETCH vu.voucher " +
           "WHERE vu.booking.id = :bookingId AND vu.status = 'USED'")
    Optional<VoucherUsage> findUsedByBookingId(@Param("bookingId") Long bookingId);

    long countByVoucherIdAndCustomerId(Integer voucherId, Integer customerId);
}
