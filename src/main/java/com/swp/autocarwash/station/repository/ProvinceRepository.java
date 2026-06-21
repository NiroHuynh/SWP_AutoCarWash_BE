package com.swp.autocarwash.station.repository;

import com.swp.autocarwash.station.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * Chức năng: ProvinceRepository cung cấp tầng truy vấn dữ liệu cho entity Province.
 *
 * Repository này chịu trách nhiệm thao tác với database để kiểm tra và truy xuất
 * thông tin các province trong hệ thống.
 *
 * @author Phong
 * @version 1.0
 */
public interface ProvinceRepository extends JpaRepository<Province, Integer> {
    /**
     *
     * Chức năng: Kiểm tra sự tồn tại của province theo id.
     *
     * Quy trình:
     * - Nhận provinceId từ service layer hoặc controller.
     * - Thực hiện query kiểm tra record tồn tại trong database.
     * - Trả về true nếu tồn tại, ngược lại trả về false.
     *
     * @param id id của province cần kiểm tra
     *
     * @return true nếu province tồn tại, false nếu không tồn tại
     *
     * @author Phong
     * @version 1.0
     */
    boolean existsById(Long id);
}
