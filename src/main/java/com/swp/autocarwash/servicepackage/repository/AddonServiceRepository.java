package com.swp.autocarwash.servicepackage.repository;

import com.swp.autocarwash.servicepackage.entity.AddonService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 *
 * Chức năng: AddonServiceRepository dùng để truy xuất và thao tác dữ liệu
 * của AddonService entity trong database.
 *
 * Repository cung cấp các phương thức truy vấn addon service theo trạng thái
 * và danh sách id phục vụ cho các nghiệp vụ như booking, tính giá và lấy danh sách addon.
 *
 * @author Phong
 * @version 1.0
 */
@Repository
public interface AddonServiceRepository
        extends JpaRepository<AddonService, Integer> {


    /**
     *
     * Chức năng: Lấy danh sách addon service đang hoạt động trong hệ thống.
     *
     * Quy trình:
     * - Thực hiện truy vấn database theo điều kiện isDeleted = false.
     * - Loại bỏ các addon service đã bị đánh dấu xóa mềm.
     * - Trả về danh sách addon service hợp lệ.
     *
     * @return danh sách addon service chưa bị xóa
     *
     * @author Phong
     * @version 1.0
     */
    List<AddonService> findByIsDeletedFalse();


    /**
     *
     * Chức năng: Lấy danh sách addon service theo nhiều id.
     *
     * Quy trình:
     * - Nhận danh sách addon id cần tìm.
     * - Truy vấn các addon có id nằm trong danh sách.
     * - Chỉ lấy các addon chưa bị xóa mềm.
     * - Trả về danh sách addon service tìm được.
     *
     * @param ids danh sách id addon service cần lấy
     *
     * @return danh sách addon service tương ứng với ids
     *
     * @author Phong
     * @version 1.0
     */
    List<AddonService> findByIdInAndIsDeletedFalse(
            List<Integer> ids
    );

    /**
     * Kiểm tra addon có đang được service_package (chưa xóa) nào sử dụng không
     * Qua bảng package_addon_mapping JOIN service_package (is_deleted = false)
     */
    @Query("""
    SELECT COUNT(m) > 0
    FROM PackageAddonMapping m
    WHERE m.addonService.id = :addonId
      AND m.servicePackage.isDeleted = false
""")
    boolean isUsedByActiveServicePackage(@Param("addonId") Integer addonId);

    /**
     * Kiểm tra addon name đã tồn tại (chưa xóa)
     */
    boolean existsByNameAndIsDeletedFalse(String name);

    /**
     * Kiểm tra addon name đã tồn tại (chưa xóa), loại trừ chính nó (dùng cho update)
     */
    boolean existsByNameAndIsDeletedFalseAndIdNot(String name, Integer id);
}
