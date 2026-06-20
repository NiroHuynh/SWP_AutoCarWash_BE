package com.swp.autocarwash.station.repository;

import com.swp.autocarwash.station.entity.Commune;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

/**
 *
 * Chức năng: CommuneRepository cung cấp tầng truy vấn dữ liệu cho entity Commune.
 *
 * Repository này chịu trách nhiệm tương tác với database để lấy thông tin
 * các commune theo province hoặc các điều kiện liên quan.
 *
 * @author Phong
 * @version 1.0
 */
public interface CommuneRepository extends JpaRepository<Commune, Integer> {

    /**
     *
     * Chức năng: Lấy danh sách commune theo provinceId.
     *
     * Quy trình:
     * - Nhận provinceId từ service layer.
     * - Thực hiện query database để lấy tất cả commune thuộc province tương ứng.
     * - Trả về danh sách Commune entity.
     *
     * @param provinceId id của province cần truy vấn
     *
     * @return danh sách Commune thuộc province
     *
     * @author Phong
     * @version 1.0
     */
    List<Commune> findByProvinceId(Integer provinceId);
}
