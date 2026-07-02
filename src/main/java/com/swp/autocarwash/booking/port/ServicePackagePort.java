package com.swp.autocarwash.booking.port;

import com.swp.autocarwash.booking.dto.response.BookingContextResponse;
import com.swp.autocarwash.common.contract.servicepackage.ServicePackageContract;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * Chức năng: ServicePackagePort định nghĩa các phương thức giao tiếp giữa booking module
 * và module quản lý service package. Interface này cung cấp thông tin gói dịch vụ,
 * thời lượng, giá và dữ liệu cần thiết phục vụ quá trình tạo booking.
 *
 * @author Phong
 * @version 1.0
 */
public interface ServicePackagePort {
    /**
     *
     * Chức năng: Lấy toàn bộ danh sách service package hiện có trong hệ thống.
     *
     * Quy trình:
     * - Gửi yêu cầu lấy danh sách service package.
     * - Nhận dữ liệu package từ module quản lý dịch vụ.
     * - Trả về toàn bộ danh sách package không áp dụng filter theo station.
     *
     * @return danh sách ServicePackageContract chứa thông tin các gói dịch vụ
     *
     * @author Phong
     * @version 1.0
     */
    List<BookingContextResponse.ServicePackageDTO> getAllPackages();

    /**
     *
     * Chức năng: Lấy thời lượng thực hiện của một service package.
     *
     * Quy trình:
     * - Nhận servicePackageId cần lấy duration.
     * - Tìm kiếm thông tin service package tương ứng.
     * - Lấy thời lượng thực hiện của package.
     * - Trả về thời lượng dưới dạng phút.
     *
     * @param servicePackageId id của service package cần lấy thời lượng
     *
     * @return thời lượng thực hiện của service package (tính theo phút)
     *
     * @author Phong
     * @version 1.0
     */
    Integer getDuration(Integer servicePackageId);

    /**
     *
     * Chức năng: Lấy thông tin chi tiết của service package theo id.
     *
     * Quy trình:
     * - Nhận id của service package.
     * - Truy vấn dữ liệu package tương ứng.
     * - Mapping dữ liệu sang ServicePackageContract.
     * - Trả về thông tin package.
     *
     * @param id id của service package cần lấy thông tin
     *
     * @return ServicePackageContract chứa thông tin chi tiết service package
     *
     * @author Phong
     * @version 1.0
     */
    ServicePackageContract getServicePackage(Integer id);

    /**
     *
     * Chức năng: Lấy thông tin service package phục vụ quá trình booking,
     * bao gồm giá và các thông tin liên quan đến slot cần sử dụng.
     *
     * Quy trình:
     * - Nhận id service package.
     * - Truy vấn thông tin package.
     * - Lấy dữ liệu giá, duration và yêu cầu slot.
     * - Trả về ServicePackageContract để booking xử lý.
     *
     * @param id id của service package cần lấy thông tin
     *
     * @return ServicePackageContract chứa thông tin package cần cho booking
     *
     * @author Phong
     * @version 1.0
     */
    ServicePackageContract getById(Integer id);
}
