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
 * Adapter thật giao tiếp addon service với booking module
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
     * Lấy toàn bộ addon service
     *
     */
    @Override
    public List<AddonServiceContract> getAllAddons(){
        return addonServiceService.getAll();
    }




    /**
     *
     * Tính tổng thời gian addon
     *
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
     * Tính tổng giá addon
     *
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
     * Lấy addon theo danh sách id
     *
     */
    @Override
    public List<AddonServiceContract> getByIds(
            List<Integer> ids
    ){

        return addonServiceService
                .getByIds(ids);

    }
}