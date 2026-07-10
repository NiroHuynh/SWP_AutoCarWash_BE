package com.swp.autocarwash.promotion.repository;

import com.swp.autocarwash.promotion.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PromotionRepository extends JpaRepository<Promotion, Integer> {

    // Tìm chiến dịch Chế độ 1 thỏa mãn cả Chi nhánh, Khung ngày hẹn VÀ Hạng thành viên của khách
    @Query(value = "SELECT p.* FROM promotion p " +
            "JOIN promotion_station_mapping psm ON p.id = psm.promotion_id " +
            "LEFT JOIN promotion_target_mapping ptm ON p.id = ptm.promotion_id " +
            "WHERE psm.station_id = :stationId " +
            "AND p.is_deleted = false AND p.status = 'ACTIVE' " +
            "AND :appDate BETWEEN p.start_date AND p.end_date " +
            "AND (ptm.promotion_target_id IS NULL OR ptm.promotion_target_id = :customerTierId)",
            nativeQuery = true)
    List<Promotion> findActiveDirectPromotionsForUser( // Đổi thành số nhiều List
                                                       @Param("stationId") Integer stationId,
                                                       @Param("appDate") LocalDate appDate,
                                                       @Param("customerTierId") Integer customerTierId);
    Optional<Promotion> findByIdAndIsDeletedFalse(Integer id);

}
