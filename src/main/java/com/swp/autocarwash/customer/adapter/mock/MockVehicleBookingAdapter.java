package com.swp.autocarwash.customer.adapter.mock;

import com.swp.autocarwash.booking.port.VehiclePort;
import com.swp.autocarwash.common.contract.customer.VehicleContract;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * Chức năng: MockVehicleBookingAdapter là adapter giả lập triển khai VehiclePort
 * trong môi trường development. Class này cung cấp dữ liệu vehicle mẫu, kiểm tra
 * quyền sở hữu xe và lấy thông tin vehicle mà không cần kết nối tới vehicle module thật.
 *
 * @author Phong
 * @version 1.0
 */
@Component
@Profile("dev")
public class MockVehicleBookingAdapter{


}
