package com.swp.autocarwash.servicepackage.repository;

import com.swp.autocarwash.servicepackage.entity.ServicePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


/**
 *
 * Repository thao tác dữ liệu service package
 *
 * @author Phong
 * @version 1.0
 */
public interface ServicePackageRepository
        extends JpaRepository<ServicePackage, Integer> {


    /**
     *
     * Lấy toàn bộ service package đang hoạt động
     *
     * @return danh sách service package
     *
     */
    List<ServicePackage> findByIsDeletedFalse();



    /**
     *
     * Tìm service package theo id và trạng thái active
     *
     * @param id service package id
     *
     * @return service package
     *
     */
    Optional<ServicePackage> findByIdAndIsDeletedFalse(
            Integer id
    );

}
