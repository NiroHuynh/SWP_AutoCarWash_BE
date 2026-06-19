package com.swp.autocarwash.station.repository;

import com.swp.autocarwash.station.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

/**
 * Repository truy xuất dữ liệu Station
 */
public interface StationRepository extends JpaRepository<Station, Long> {

    /**
     * Lấy danh sách station theo communeId
     * @param communeId id của commune
     * @return list station thuộc commune
     */
    List<Station> findByCommuneId(Integer communeId);
}
