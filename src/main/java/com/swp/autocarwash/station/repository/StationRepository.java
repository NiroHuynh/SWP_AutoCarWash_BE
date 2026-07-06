package com.swp.autocarwash.station.repository;

import com.swp.autocarwash.station.entity.Station;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;


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
}
