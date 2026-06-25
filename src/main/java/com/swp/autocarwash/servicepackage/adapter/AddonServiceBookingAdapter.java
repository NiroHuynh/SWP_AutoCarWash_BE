package com.swp.autocarwash.servicepackage.adapter;

import com.swp.autocarwash.booking.port.AddonServicePort;
import com.swp.autocarwash.common.contract.servicepackage.AddonServiceContract;
import com.swp.autocarwash.servicepackage.service.AddonServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;


/**
 *
 * Chức năng: AddonServiceBookingAdapter là adapter production triển khai
 * AddonServicePort, đóng vai trò cầu nối giữa booking module và addon service module.
 *
 * Class này chịu trách nhiệm expose các nghiệp vụ addon service cho booking module
 * thông qua contract layer.
 *
 * @author Phong
 * @version 1.0
 */
@Component
@Profile("pro")
@RequiredArgsConstructor
public class AddonServiceBookingAdapter
        implements AddonServicePort {


    private final AddonServiceService addonServiceService;



    /**
     *
     * Chức năng: Lấy danh sách toàn bộ addon service trong hệ thống.
     *
     * Quy trình:
     * - Gọi AddonServiceService để lấy dữ liệu addon.
     * - Service xử lý truy vấn và mapping dữ liệu.
     * - Trả về danh sách AddonServiceContract cho booking module.
     *
     * @return danh sách AddonServiceContract
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public List<AddonServiceContract> getAllAddons(){
        return addonServiceService.getAll();
    }




    /**
     *
     * Chức năng: Tính tổng thời gian thực hiện của các addon được chọn.
     *
     * Quy trình:
     * - Nhận danh sách addonId.
     * - Gọi AddonServiceService xử lý tính toán duration.
     * - Trả về tổng thời gian addon theo phút.
     *
     * @param addonIds danh sách id addon service cần tính thời gian
     *
     * @return tổng thời gian addon service (phút)
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public Integer getTotalDuration(
            List<Integer> addonIds
    ){

        return addonServiceService
                .calculateDuration(addonIds);

    }




    /**
     *
     * Chức năng: Tính tổng giá tiền của các addon service.
     *
     * Quy trình:
     * - Nhận danh sách addonId được chọn.
     * - Gọi AddonServiceService lấy giá từng addon.
     * - Tính tổng giá trị addon.
     * - Trả về tổng giá addon.
     *
     * @param addonIds danh sách id addon service cần tính giá
     *
     * @return tổng giá addon service
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public BigDecimal calculateAddonPrice(
            List<Integer> addonIds
    ){

        return addonServiceService
                .calculatePrice(addonIds);

    }




    /**
     *
     * Chức năng: Lấy danh sách addon service theo danh sách id.
     *
     * Quy trình:
     * - Nhận danh sách addon id cần lấy.
     * - Gửi request đến AddonServiceService.
     * - Service truy vấn addon tương ứng.
     * - Trả về danh sách AddonServiceContract.
     *
     * @param ids danh sách id addon service cần tìm
     *
     * @return danh sách AddonServiceContract theo ids
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public List<AddonServiceContract> getByIds(
            List<Integer> ids
    ){

        return addonServiceService
                .getByIds(ids);

    }
}