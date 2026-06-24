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
 * @author Phong
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

    /**
     *
     * Chức năng: Đếm số lần một voucher đã được customer sử dụng.
     *
     * Quy trình:
     * - Nhận voucherId và customerId cần kiểm tra.
     * - Truy vấn số lượng VoucherUsage tương ứng.
     * - Trả về số lần voucher đã được customer sử dụng.
     *
     * @param voucherId id của voucher cần kiểm tra
     * @param customerId id của customer sử dụng voucher
     *
     * @return số lượng lần voucher được sử dụng bởi customer
     *
     * @author Phong
     * @version 1.0
     */
    long countByVoucherIdAndCustomerId(Long voucherId, Long customerId);

    /**
     * kiểm tra xem customer này có dùng voucher này trước đó hay chưa
     *
     * @param voucherId
     * @param customerId
     * @return boolean
     */
    boolean existsByVoucherIdAndCustomerId(
            Long voucherId,
            Long customerId
    );
}
