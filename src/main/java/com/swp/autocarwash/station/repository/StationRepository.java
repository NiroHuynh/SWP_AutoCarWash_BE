package com.swp.autocarwash.station.repository;

import com.swp.autocarwash.station.entity.Station;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

/**
 *
 * Chức năng: StationRepository cung cấp tầng truy vấn dữ liệu cho entity Station.
 *
 * Repository này chịu trách nhiệm thao tác với database để lấy danh sách station
 * theo commune hoặc các điều kiện liên quan.
 *
 * @author Phong
 * @version 1.0
 */
public interface StationRepository extends JpaRepository<Station, Integer> {

    /**
     *
     * Chức năng: Lấy danh sách station theo communeId.
     *
     * Quy trình:
     * - Nhận communeId từ service layer.
     * - Thực hiện query database để tìm tất cả station thuộc commune tương ứng.
     * - Trả về danh sách Station entity.
     *
     * @param communeId id của commune cần truy vấn
     *
     * @return danh sách Station thuộc commune
     *
     * @author Phong
     * @version 1.0
     */
    List<Station> findByCommuneId(Integer communeId);

    Optional<Station> findByIdAndIsDeletedFalse(@NotNull(message = "Station Id can not be null") Integer stationId);

//    @Query(value = "SELECT s.id, s.station_name, COUNT(psm.promotion_id) AS totalActivePromotions " +
//            "FROM station s " +
//            "LEFT JOIN promotion_station_mapping psm ON s.id = psm.station_id " +
//            "LEFT JOIN promotion p ON psm.promotion_id = p.id AND (:status = 'ALL' OR p.status = :status) " +
//            "WHERE s.is_deleted = false AND s.is_operating = true " +
//            "GROUP BY s.id, s.station_name", nativeQuery = true)
//    List<Object[]> countPromotionsPerBranch(@Param("status") String status);

    @Query(value = "SELECT s.id, s.station_name, " +
            "COUNT(CASE WHEN p.id IS NOT NULL AND p.is_deleted = false AND (:status = 'ALL' OR p.status = :status) THEN 1 END) AS totalActivePromotions " +
            "FROM station s " +
            "LEFT JOIN promotion_station_mapping psm ON s.id = psm.station_id " +
            "LEFT JOIN promotion p ON psm.promotion_id = p.id " +
            "WHERE s.is_deleted = false AND s.is_operating = true " +
            "GROUP BY s.id, s.station_name", nativeQuery = true)
    List<Object[]> countPromotionsPerBranch(@Param("status") String status);

    List<Station> findByIsDeletedFalse();
}
