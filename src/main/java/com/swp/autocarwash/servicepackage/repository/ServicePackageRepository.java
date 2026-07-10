package com.swp.autocarwash.servicepackage.repository;

import com.swp.autocarwash.servicepackage.entity.ServicePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 *
 * Chức năng: ServicePackageRepository dùng để truy xuất và thao tác dữ liệu
 * của ServicePackage entity trong database.
 *
 * Repository cung cấp các phương thức truy vấn service package theo trạng thái
 * hoạt động và tìm kiếm package theo id phục vụ cho các nghiệp vụ booking.
 *
 * @author Phong
 * @version 1.0
 */
@Repository
public interface ServicePackageRepository
        extends JpaRepository<ServicePackage, Integer> {


    /**
     *
     * Chức năng: Lấy danh sách toàn bộ service package đang hoạt động.
     *
     * Quy trình:
     * - Thực hiện truy vấn database theo điều kiện isDeleted = false.
     * - Loại bỏ các service package đã bị xóa mềm.
     * - Trả về danh sách package hợp lệ trong hệ thống.
     *
     * @return danh sách service package đang active
     *
     * @author Phong
     * @version 1.0
     */
    List<ServicePackage> findByIsDeletedFalse();



    /**
     *
     * Chức năng: Tìm service package theo id với điều kiện package chưa bị xóa.
     *
     * Quy trình:
     * - Nhận service package id cần tìm.
     * - Kiểm tra package có tồn tại trong database.
     * - Kiểm tra trạng thái isDeleted = false.
     * - Trả về Optional chứa service package nếu hợp lệ.
     *
     * @param id service package id cần tìm
     *
     * @return Optional chứa ServicePackage nếu tồn tại và đang hoạt động
     *
     * @author Phong
     * @version 1.0
     */
    Optional<ServicePackage> findByIdAndIsDeletedFalse(
            Integer id
    );

    Optional<ServicePackage> findById(int id);


    /**
     * Lấy tất cả package chưa xóa + tính tổng duration từ addon mapping
     * LEFT JOIN để package không có addon vẫn trả về (durationMinutes = 0)
     */
    @Query(value = """
        SELECT sp.id, sp.name, sp.description, sp.base_price,
               COALESCE(SUM(a.duration_minutes), 0) AS duration_minutes
        FROM service_package sp
        LEFT JOIN package_addon_mapping m ON m.service_package_id = sp.id
        LEFT JOIN addon_service a ON a.id = m.addon_service_id
        WHERE sp.is_deleted = false
        GROUP BY sp.id, sp.name, sp.description, sp.base_price
    """, nativeQuery = true)
    List<Object[]> findAllWithDuration();

    /**
     * Kiểm tra name đã tồn tại (chưa xóa) — dùng cho create
     */
    boolean existsByNameAndIsDeletedFalse(String name);

    /**
     * Kiểm tra name đã tồn tại (chưa xóa), loại trừ chính nó — dùng cho update
     */
    boolean existsByNameAndIsDeletedFalseAndIdNot(String name, Integer id);

}
