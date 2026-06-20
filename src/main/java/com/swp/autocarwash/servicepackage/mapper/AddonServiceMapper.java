package com.swp.autocarwash.servicepackage.mapper;

import com.swp.autocarwash.common.contract.servicepackage.AddonServiceContract;
import com.swp.autocarwash.servicepackage.entity.AddonService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


/**
 *
 * Chức năng: AddonServiceMapper dùng để chuyển đổi dữ liệu giữa AddonService entity
 * và AddonServiceContract.
 *
 * Class này hỗ trợ việc giao tiếp giữa addon service module và các module khác
 * thông qua contract layer, giúp tránh phụ thuộc trực tiếp vào entity.
 *
 * @author Phong
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class AddonServiceMapper {


    private final ModelMapper modelMapper;


    /**
     *
     * Chức năng: Chuyển đổi AddonService entity sang AddonServiceContract.
     *
     * Quy trình:
     * - Nhận AddonService entity từ service layer.
     * - Sử dụng ModelMapper để mapping dữ liệu entity sang contract.
     * - Trả về AddonServiceContract phục vụ giao tiếp giữa các module.
     *
     * @param entity addon service entity cần chuyển đổi
     *
     * @return AddonServiceContract chứa thông tin addon service
     *
     * @author Phong
     * @version 1.0
     */
    public AddonServiceContract toContract(
            AddonService entity
    ){

        return modelMapper.map(
                entity,
                AddonServiceContract.class
        );
    }
}