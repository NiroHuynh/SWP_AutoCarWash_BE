package com.swp.autocarwash.servicepackage.repository;

import com.swp.autocarwash.servicepackage.entity.AddonService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 *
 * Repository xử lý truy xuất dữ liệu addon service
 *
 * @author Phong
 * @version 1.0
 */
@Repository
public interface AddonServiceRepository
        extends JpaRepository<AddonService, Integer> {


    /**
     *
     * Lấy danh sách addon service chưa bị xóa
     *
     * @return danh sách addon đang active
     *
     */
    List<AddonService> findByIsDeletedFalse();


    /**
     *
     * Lấy danh sách addon theo nhiều id
     *
     * @param ids danh sách addon id
     * @return danh sách addon
     *
     */
    List<AddonService> findByIdInAndIsDeletedFalse(
            List<Integer> ids
    );
}
