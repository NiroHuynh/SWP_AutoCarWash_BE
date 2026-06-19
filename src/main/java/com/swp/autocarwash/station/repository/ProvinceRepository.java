package com.swp.autocarwash.station.repository;

import com.swp.autocarwash.station.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProvinceRepository extends JpaRepository<Province, Integer> {
    /**
     * Kiểm tra tồn tại province
     * Purpose: validate input provinceId
     */
    boolean existsById(Long id);
}
