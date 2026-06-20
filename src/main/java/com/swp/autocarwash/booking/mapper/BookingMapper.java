package com.swp.autocarwash.booking.mapper;

import com.swp.autocarwash.booking.dto.response.BookingContextResponse;
import com.swp.autocarwash.common.contract.customer.VehicleContract;
import com.swp.autocarwash.common.contract.promotion.VoucherContract;
import com.swp.autocarwash.common.contract.servicepackage.AddonServiceContract;
import com.swp.autocarwash.common.contract.servicepackage.ServicePackageContract;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 *
 * Chức năng: BookingMapper dùng để chuyển đổi dữ liệu giữa các Contract object
 * và DTO object trong module booking. Class này đảm nhiệm việc mapping dữ liệu
 * phục vụ cho việc trả response từ booking service.
 *
 * @author Phong
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class BookingMapper {

    private final ModelMapper modelMapper;

    /**
     *
     * Chức năng: Chuyển đổi thông tin VehicleContract sang VehicleDTO để trả về client.
     *
     * Quy trình:
     * - Nhận dữ liệu phương tiện từ VehicleContract.
     * - Sử dụng ModelMapper để mapping các field tương ứng.
     * - Trả về đối tượng VehicleDTO sau khi chuyển đổi.
     *
     * @param c thông tin phương tiện từ contract layer
     *
     * @return VehicleDTO chứa thông tin phương tiện cần hiển thị
     *
     * @author Phong
     * @version 1.0
     */
    public BookingContextResponse.VehicleDTO toVehicleDTO(VehicleContract c) {
        return modelMapper.map(c, BookingContextResponse.VehicleDTO.class);
    }

    /**
     *
     * Chức năng: Chuyển đổi thông tin ServicePackageContract sang ServicePackageDTO.
     *
     * Quy trình:
     * - Nhận thông tin gói dịch vụ từ ServicePackageContract.
     * - Mapping dữ liệu sang ServicePackageDTO.
     * - Trả về dữ liệu gói dịch vụ đã được chuyển đổi.
     *
     * @param c thông tin service package từ contract layer
     *
     * @return ServicePackageDTO chứa thông tin gói dịch vụ
     *
     * @author Phong
     * @version 1.0
     */
    public BookingContextResponse.ServicePackageDTO toPackage(ServicePackageContract c) {
        return modelMapper.map(c, BookingContextResponse.ServicePackageDTO.class);
    }

    /**
     *
     * Chức năng: Chuyển đổi thông tin AddonServiceContract sang AddonServiceDTO.
     *
     * Quy trình:
     * - Nhận dữ liệu addon service từ contract layer.
     * - Mapping dữ liệu sang AddonServiceDTO.
     * - Trả về thông tin addon service sau khi chuyển đổi.
     *
     * @param c thông tin addon service từ contract layer
     *
     * @return AddonServiceDTO chứa thông tin addon service
     *
     * @author Phong
     * @version 1.0
     */
    public BookingContextResponse.AddonServiceDTO toAddon(AddonServiceContract c) {
        return modelMapper.map(c, BookingContextResponse.AddonServiceDTO.class);
    }

    /**
     *
     * Chức năng: Chuyển đổi thông tin VoucherContract sang VoucherDTO.
     *
     * Quy trình:
     * - Nhận dữ liệu voucher từ contract layer.
     * - Mapping dữ liệu sang VoucherDTO.
     * - Trả về VoucherDTO sau khi chuyển đổi.
     *
     * @param c thông tin voucher từ contract layer
     *
     * @return VoucherDTO chứa thông tin voucher
     *
     * @author Phong
     * @version 1.0
     */
    public BookingContextResponse.VoucherDTO toVoucher(VoucherContract c) {
        return modelMapper.map(c, BookingContextResponse.VoucherDTO.class);
    }


}
