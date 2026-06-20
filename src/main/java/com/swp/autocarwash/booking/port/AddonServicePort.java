package com.swp.autocarwash.booking.port;

import com.swp.autocarwash.common.contract.servicepackage.AddonServiceContract;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * Chức năng: AddonServicePort định nghĩa các phương thức giao tiếp giữa booking module
 * và module quản lý addon service. Interface này cung cấp dữ liệu addon service,
 * tính toán thời lượng và chi phí addon phục vụ quá trình tạo booking.
 *
 * @author Phong
 * @version 1.0
 */
public interface AddonServicePort {
    /**
     *
     * Chức năng: Lấy toàn bộ danh sách addon service hiện có trong hệ thống.
     *
     * Quy trình:
     * - Gửi yêu cầu lấy danh sách addon thông qua port.
     * - Nhận dữ liệu addon từ module triển khai.
     * - Trả về danh sách addon service.
     *
     * @return danh sách AddonServiceContract chứa thông tin các addon service
     *
     * @author Phong
     * @version 1.0
     */
    List<AddonServiceContract> getAllAddons();

    /**
     *
     * Chức năng: Tính tổng thời lượng của các addon service được chọn.
     *
     * Quy trình:
     * - Nhận danh sách addonId cần tính toán.
     * - Tìm thông tin thời lượng tương ứng của từng addon.
     * - Cộng dồn thời lượng của tất cả addon.
     * - Trả về tổng thời lượng addon.
     *
     * @param addonIds danh sách id của các addon service cần tính thời lượng
     *
     * @return tổng thời lượng của các addon service (tính theo phút)
     *
     * @author Phong
     * @version 1.0
     */
    Integer getTotalDuration(List<Integer> addonIds);

    /**
     *
     * Chức năng: Tính tổng chi phí của các addon service được chọn trong booking.
     *
     * Quy trình:
     * - Nhận danh sách addonId từ yêu cầu booking.
     * - Lấy giá của từng addon service.
     * - Cộng tổng giá trị các addon.
     * - Trả về tổng chi phí addon.
     *
     * @param Ids danh sách id của các addon service cần tính giá
     *
     * @return tổng giá tiền của các addon service
     *
     * @author Phong
     * @version 1.0
     */
    BigDecimal calculateAddonPrice(List<Integer> addonIds);

    /**
     *
     * Chức năng: Lấy danh sách addon service theo danh sách id được cung cấp.
     *
     * Quy trình:
     * - Nhận danh sách id addon cần tìm.
     * - Truy vấn dữ liệu addon tương ứng.
     * - Mapping dữ liệu sang AddonServiceContract.
     * - Trả về danh sách addon tìm được.
     *
     * @param ids danh sách id của addon service cần lấy
     *
     * @return danh sách AddonServiceContract tương ứng với các id truyền vào
     *
     * @author Phong
     * @version 1.0
     */
    List<AddonServiceContract> getByIds(List<Integer> ids);
}
