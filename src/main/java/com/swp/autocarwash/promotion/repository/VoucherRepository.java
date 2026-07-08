package com.swp.autocarwash.promotion.repository;

import com.swp.autocarwash.promotion.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 *
 * Chức năng: VoucherRepository cung cấp các phương thức truy cập dữ liệu
 * cho Voucher entity. Repository này hỗ trợ thao tác CRUD, tìm kiếm voucher
 * và kiểm tra sự tồn tại của voucher trong database.
 *
 * @author Phong
 * @version 1.0
 */
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {

    /**
     *
     * Chức năng: Tìm kiếm voucher theo mã voucher code.
     *
     * Quy trình:
     * - Nhận voucherCode cần tìm.
     * - Thực hiện truy vấn voucher trong database.
     * - Trả về Optional chứa Voucher nếu tồn tại.
     * - Trả về Optional.empty() nếu không tìm thấy voucher.
     *
     * @param code mã voucher cần tìm kiếm
     *
     * @return Optional<Voucher> chứa voucher nếu tồn tại
     *
     * @author Phong
     * @version 1.0
     */
    Optional<Voucher> findByVoucherCode(String code);

    /**
     * lấy tất cả các voucher đang còn hạn
     * @param now
     * @return
     */
    @Query("""
        SELECT v 
        FROM Voucher v
        WHERE v.status = 'ACTIVE'
        AND v.startDate <= :now
        AND v.expiryDate >= :now
        AND v.usedCount < v.usageLimit
    """)
    List<Voucher> findAvailableVouchers(@Param("now") LocalDateTime now);


    @Modifying
    @Query("""
        UPDATE Voucher v
        SET v.usedCount = v.usedCount + 1
        WHERE v.id = :id
        AND v.usedCount < v.usageLimit
    """)
    int increaseUsedCount(
            Long id
    );

    Voucher findVoucherById(Long id);

    boolean existsByVoucherCode(String voucherCode);

    Optional <Voucher> findByPromotionId(Integer promotionId);

    List<Voucher> findByPromotionIdAndIsDeletedFalse(Integer promotionId);

    Optional<Voucher> findByIdAndIsDeletedFalse(Long voucherId);
}