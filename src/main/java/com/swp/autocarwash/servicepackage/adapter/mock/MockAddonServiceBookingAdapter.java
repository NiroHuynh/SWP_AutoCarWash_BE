package com.swp.autocarwash.servicepackage.adapter.mock;

import com.swp.autocarwash.booking.port.AddonServicePort;
import com.swp.autocarwash.common.contract.servicepackage.AddonServiceContract;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * Chức năng: MockAddonServiceBookingAdapter là adapter giả lập triển khai
 * AddonServicePort trong môi trường development.
 *
 * Class này cung cấp dữ liệu addon service mẫu để booking module có thể
 * hoạt động độc lập khi chưa kết nối addon service thật.
 *
 * @author Phong
 * @version 1.0
 */
@Component
@Profile("dev")
public class MockAddonServiceBookingAdapter implements AddonServicePort {

    /**
     *
     * Chức năng: Lấy danh sách addon service giả lập.
     *
     * Quy trình:
     * - Không truy vấn database hoặc service bên ngoài.
     * - Tạo danh sách addon service mẫu.
     * - Trả về danh sách AddonServiceContract.
     *
     * @return danh sách addon service giả lập
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public List<AddonServiceContract> getAllAddons() {
        return List.of(
                new AddonServiceContract(1, "Wax", new BigDecimal("20000"), 15),
                new AddonServiceContract(2, "Interior Cleaning", new BigDecimal("30000"), 20),
                new AddonServiceContract(3, "Engine Wash", new BigDecimal("50000"), 25)
        );
    }

    /**
     *
     * Chức năng: Tính tổng thời gian thực hiện của các addon được chọn.
     *
     * Quy trình:
     * - Nhận danh sách addonId.
     * - Kiểm tra danh sách addon có null hay không.
     * - Nếu null trả về 0 phút.
     * - Nếu có dữ liệu, tính duration dựa trên số lượng addon.
     * - Trả về tổng số phút addon.
     *
     * @param addonIds danh sách id addon service được chọn
     *
     * @return tổng thời gian addon tính theo phút
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public Integer getTotalDuration(List<Integer> addonIds) {
        return addonIds == null ? 0 : addonIds.size() * 15;
    }

    /**
     *
     * Chức năng: Tính tổng giá tiền của các addon service.
     *
     * Quy trình:
     * - Nhận danh sách addonId.
     * - Kiểm tra danh sách addon có null hay không.
     * - Nếu null trả về giá trị 0.
     * - Tính tổng tiền dựa trên số lượng addon.
     * - Trả về tổng giá addon.
     *
     * @param addonIds danh sách id addon service
     *
     * @return tổng giá tiền addon
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public BigDecimal calculateAddonPrice(List<Integer> addonIds) {
        if (addonIds == null) return BigDecimal.ZERO;
        return BigDecimal.valueOf(addonIds.size() * 20000);
    }

    /**
     *
     * Chức năng: Lấy danh sách addon service theo danh sách id.
     *
     * Quy trình:
     * - Nhận danh sách addonId cần tìm.
     * - Duyệt qua từng id.
     * - Tạo addon contract giả lập tương ứng.
     * - Gán thông tin giá và thời gian mặc định.
     * - Trả về danh sách addon contract.
     *
     * @param ids danh sách id addon service cần lấy
     *
     * @return danh sách AddonServiceContract theo ids
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public List<AddonServiceContract> getByIds(List<Integer> ids) {
        return ids.stream().map(id -> {
            AddonServiceContract c = new AddonServiceContract();
            c.setId(id);
            c.setPrice(new BigDecimal("20000"));
            c.setDurationMinutes(15);
            return c;
        }).toList();
    }
}
