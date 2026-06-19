package com.swp.autocarwash.station.repository;

import com.swp.autocarwash.station.entity.Commune;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface CommuneRepository extends JpaRepository<Commune, Integer> {

    /**
     * Lấy danh sách commune theo provinceId
     * Purpose: query communes thuộc 1 tỉnh
     */
    List<Commune> findByProvinceId(Integer provinceId);
}
