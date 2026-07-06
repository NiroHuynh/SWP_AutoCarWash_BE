package com.swp.autocarwash.wash.repository.custom;

import com.swp.autocarwash.wash.entity.WashLane;
import com.swp.autocarwash.wash.entity.enums.WashLaneStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WashLaneRepository extends JpaRepository<WashLane, Integer> {
    //check xem hệ thống có bất kỳ làn rửa nào ở trạng thái đc truyền vào hay ko
    Boolean existsByStatus(String status);

    //loại trừ những thằng đã bị softdeleted ra
    Boolean existsByStatusAndIsDeletedFalse(String status);

    boolean existsByStationIdAndStatusAndIsDeletedFalse(Integer stationId, String status);

    @Query("SELECT COUNT(w) > 0 FROM WashLane w" +
            " WHERE w.station.id= :stationId" +
            " AND w.isDeleted = false" +
            " AND w.laneName = :laneName")
    boolean existsByStationIdAndLaneName(
            @Param("stationId") Integer stationId,
            @Param("laneName") String laneName);
}
