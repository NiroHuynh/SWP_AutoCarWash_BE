package com.swp.autocarwash.station.repository;

import com.swp.autocarwash.station.entity.WashLane;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WashLaneRepository extends JpaRepository<WashLane, Integer> {

    /** Đếm số làn (chưa xóa) của 1 station đang ở trạng thái cho trước (vd: AVAILABLE). */
    long countByStation_IdAndStatusAndIsDeletedFalse(Integer stationId, String status);

    /** Lấy 1 làn (chưa xóa) của 1 station đang ở trạng thái cho trước, để đổi AVAILABLE ↔ OCCUPIED. */
    Optional<WashLane> findFirstByStation_IdAndStatusAndIsDeletedFalse(Integer stationId, String status);


}
