package com.swp.autocarwash.servicepackage.mapper;

import com.swp.autocarwash.common.contract.servicepackage.ServicePackageContract;
import com.swp.autocarwash.servicepackage.entity.ServicePackage;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


/**
 *
 * Chức năng: ServicePackageMapper dùng để chuyển đổi dữ liệu giữa
 * ServicePackage entity và ServicePackageContract.
 *
 * Class này đảm nhiệm việc mapping dữ liệu từ tầng persistence sang
 * contract layer để các module khác có thể sử dụng mà không phụ thuộc trực tiếp vào entity.
 *
 * @author Phong
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class ServicePackageMapper {


    private final ModelMapper modelMapper;



    /**
     *
     * Chức năng: Chuyển đổi ServicePackage entity sang ServicePackageContract.
     *
     * Quy trình:
     * - Nhận ServicePackage entity từ service layer.
     * - Sử dụng ModelMapper để mapping các thuộc tính tương ứng.
     * - Tính toán durationMinutes từ requiredSlot trong entity.
     * - Gán durationMinutes vào contract.
     * - Trả về ServicePackageContract hoàn chỉnh.
     *
     * @param entity service package entity cần chuyển đổi
     *
     * @return ServicePackageContract chứa thông tin service package
     *
     * @author Phong
     * @version 1.0
     */
    public ServicePackageContract toContract(
            ServicePackage entity
    ){

        ServicePackageContract contract =
                modelMapper.map(
                        entity,
                        ServicePackageContract.class
                );


        /*
         *
         * durationMinutes không tồn tại trực tiếp trong entity.
         * Giá trị được suy ra từ requiredSlot.
         * Mỗi slot có thời lượng 15 phút.
         *
         */
        contract.setDurationMinutes(
                entity.getRequiredSlot()*15
        );


        return contract;
    }

}
