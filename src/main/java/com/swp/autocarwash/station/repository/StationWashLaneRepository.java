package com.swp.autocarwash.station.repository;

import com.swp.autocarwash.wash.entity.WashLane;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StationWashLaneRepository extends JpaRepository<WashLane, Integer> {

    long countByStation_IdAndStatusAndIsDeletedFalse(Integer stationId, String status);

    Optional<WashLane> findFirstByStation_IdAndStatusAndIsDeletedFalse(Integer stationId, String status);
}
