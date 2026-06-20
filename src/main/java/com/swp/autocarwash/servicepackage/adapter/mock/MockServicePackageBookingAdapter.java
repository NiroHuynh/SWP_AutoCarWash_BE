package com.swp.autocarwash.servicepackage.adapter.mock;

import com.swp.autocarwash.booking.port.ServicePackagePort;
import com.swp.autocarwash.common.contract.servicepackage.ServicePackageContract;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * Chức năng: MockServicePackageBookingAdapter là adapter giả lập triển khai
 * ServicePackagePort trong môi trường development.
 *
 * Class này cung cấp dữ liệu service package mẫu để booking module có thể
 * hoạt động độc lập khi chưa kết nối service package module thật.
 *
 * @author Phong
 * @version 1.0
 */
@Component
@Profile("dev")
public class MockServicePackageBookingAdapter implements ServicePackagePort {

    /**
     *
     * Chức năng: Lấy danh sách toàn bộ service package giả lập.
     *
     * Quy trình:
     * - Không truy vấn database hoặc service bên ngoài.
     * - Tạo danh sách service package mẫu.
     * - Trả về danh sách ServicePackageContract.
     *
     * @return danh sách ServicePackageContract giả lập
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public List<ServicePackageContract> getAllPackages() {
        return List.of(
                new ServicePackageContract(1, "Basic Wash", new BigDecimal("50000"), 15),
                new ServicePackageContract(2, "Premium Wash", new BigDecimal("90000"), 25),
                new ServicePackageContract(3, "Full Detailing", new BigDecimal("150000"), 45)
        );
    }

    /**
     *
     * Chức năng: Lấy thời gian thực hiện của service package.
     *
     * Quy trình:
     * - Nhận servicePackageId cần lấy duration.
     * - Tìm kiếm duration của package.
     * - Trả về thời gian thực hiện tính theo phút.
     *
     * @param servicePackageId id của service package
     *
     * @return thời gian thực hiện service package (phút)
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public Integer getDuration(Integer servicePackageId) {
        return 15;
    }

    /**
     *
     * Chức năng: Lấy thông tin service package theo id.
     *
     * Quy trình:
     * - Nhận servicePackageId cần tìm.
     * - Tạo service package contract giả lập.
     * - Gán thông tin id, giá và thời lượng mặc định.
     * - Trả về ServicePackageContract.
     *
     * @param id id của service package cần lấy
     *
     * @return ServicePackageContract chứa thông tin package
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public ServicePackageContract getServicePackage(Integer id) {

        ServicePackageContract c = new ServicePackageContract();
        c.setId(id);
        c.setBasePrice(BigDecimal.valueOf(100000));
        c.setDurationMinutes(30);
        return c;
    }

    /**
     *
     * Chức năng: Lấy thông tin chi tiết service package theo id
     * phục vụ quá trình tạo booking.
     *
     * Quy trình:
     * - Nhận servicePackageId.
     * - Tạo contract giả lập tương ứng.
     * - Gán giá và thời lượng mặc định.
     * - Trả về service package contract.
     *
     * @param id id của service package cần lấy
     *
     * @return ServicePackageContract chứa thông tin package
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public ServicePackageContract getById(Integer id) {
        ServicePackageContract c = new ServicePackageContract();
        c.setId(id);
        c.setBasePrice(new BigDecimal("100000"));
        c.setDurationMinutes(30);
        return c;
    }
}
