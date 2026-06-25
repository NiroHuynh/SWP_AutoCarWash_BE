package com.swp.autocarwash.customer.adapter;

import com.swp.autocarwash.booking.port.VehiclePort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * Chức năng: VehicleBookingAdapter là adapter triển khai VehiclePort trong môi trường production.
 * Class này đóng vai trò cầu nối giữa booking module và vehicle module thông qua VehicleService.
 *
 * Adapter chịu trách nhiệm expose các nghiệp vụ vehicle như lấy danh sách xe,
 * kiểm tra quyền sở hữu và lấy thông tin chi tiết vehicle cho booking flow.
 *
 * @author Phong
 * @version 1.0
 */
@Component
@Profile("pro")
@RequiredArgsConstructor
public class VehicleBookingAdapter implements VehiclePort {


}
