package com.swp.autocarwash.booking.mapper;

import com.swp.autocarwash.booking.dto.response.BookingContextResponse;
import com.swp.autocarwash.common.contract.customer.VehicleContract;
import com.swp.autocarwash.common.contract.promotion.VoucherContract;
import com.swp.autocarwash.common.contract.servicepackage.AddonServiceContract;
import com.swp.autocarwash.common.contract.servicepackage.ServicePackageContract;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingMapper {

    private final ModelMapper modelMapper;

    public BookingContextResponse.VehicleDTO toVehicleDTO(VehicleContract c) {
        return modelMapper.map(c, BookingContextResponse.VehicleDTO.class);
    }

    public BookingContextResponse.ServicePackageDTO toPackage(ServicePackageContract c) {
        return modelMapper.map(c, BookingContextResponse.ServicePackageDTO.class);
    }

    public BookingContextResponse.AddonServiceDTO toAddon(AddonServiceContract c) {
        return modelMapper.map(c, BookingContextResponse.AddonServiceDTO.class);
    }

    public BookingContextResponse.VoucherDTO toVoucher(VoucherContract c) {
        return modelMapper.map(c, BookingContextResponse.VoucherDTO.class);
    }
}
