package com.swp.autocarwash.servicepackage.repository;

import com.swp.autocarwash.servicepackage.entity.ServicePackage;
import org.springframework.data.jpa.repository.JpaRepository;
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

}
