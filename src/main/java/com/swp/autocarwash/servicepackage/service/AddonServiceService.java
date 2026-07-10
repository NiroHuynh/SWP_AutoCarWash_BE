package com.swp.autocarwash.servicepackage.service;

import com.swp.autocarwash.common.contract.servicepackage.AddonServiceContract;
import com.swp.autocarwash.servicepackage.dto.request.CreateAddonServiceRequest;

import java.math.BigDecimal;
import java.util.List;


/**
 *
 * Chức năng: AddonServiceService định nghĩa các nghiệp vụ xử lý liên quan
 * đến addon service trong hệ thống.
 *
 * Interface này cung cấp các phương thức để lấy dữ liệu addon, truy vấn addon
 * theo danh sách id, tính thời gian và chi phí của addon phục vụ cho booking flow.
 *
 * @author Phong
 * @version 1.0
 */
public interface AddonServiceService {


    /**
     *
     * Chức năng: Lấy danh sách toàn bộ addon service đang có trong hệ thống.
     *
     * Quy trình:
     * - Gọi repository lấy danh sách addon hợp lệ.
     * - Mapping dữ liệu entity sang AddonServiceContract.
     * - Trả về danh sách addon service cho module sử dụng.
     *
     * @return danh sách AddonServiceContract
     *
     * @author Phong
     * @version 1.0
     */
    List<AddonServiceContract> getAll();


    /**
     *
     * Chức năng: Lấy thông tin addon service theo danh sách id.
     *
     * Quy trình:
     * - Nhận danh sách addon id cần tìm.
     * - Truy vấn các addon tương ứng trong database.
     * - Mapping entity sang contract.
     * - Trả về danh sách addon tìm được.
     *
     * @param ids danh sách id addon service
     *
     * @return danh sách AddonServiceContract tương ứng
     *
     * @author Phong
     * @version 1.0
     */
    List<AddonServiceContract> getByIds(
            List<Integer> ids
    );



    /**
     *
     * Chức năng: Tính tổng thời gian thực hiện của các addon service được chọn.
     *
     * Quy trình:
     * - Nhận danh sách addon id.
     * - Lấy thông tin duration của từng addon.
     * - Cộng tổng thời gian thực hiện.
     * - Trả về tổng số phút cần thiết.
     *
     * @param ids danh sách id addon service cần tính thời gian
     *
     * @return tổng thời gian addon tính theo phút
     *
     * @author Phong
     * @version 1.0
     */
    Integer calculateDuration(
            List<Integer> ids
    );


    /**
     *
     * Chức năng: Tính tổng chi phí của các addon service được chọn.
     *
     * Quy trình:
     * - Nhận danh sách addon id.
     * - Lấy giá tiền từng addon service.
     * - Tính tổng giá trị các addon.
     * - Trả về tổng tiền addon.
     *
     * @param ids danh sách id addon service cần tính giá
     *
     * @return tổng giá tiền addon service
     *
     * @author Phong
     * @version 1.0
     */
    BigDecimal calculatePrice(
            List<Integer> ids
    );

    /**
     * Tạo mới 1 add-on service - dùng chung được ngay cho mọi gói (subscription plan) khác,
     * vì đây là 1 dòng addon_service thật trong DB (không riêng cho gói nào).
     */
    AddonServiceContract create(CreateAddonServiceRequest request);
}
