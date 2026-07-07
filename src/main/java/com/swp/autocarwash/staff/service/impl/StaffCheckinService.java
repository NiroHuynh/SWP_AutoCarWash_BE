package com.swp.autocarwash.staff.service.impl;

import com.swp.autocarwash.staff.dto.response.CheckInResultResponse;
import com.swp.autocarwash.staff.dto.response.ScanVehicleResponse;

public interface StaffCheckinService {
    //xử lí logic khi staff quét biển số xe tại quầy
    ScanVehicleResponse scanVehicle(String licensePlate);
    //Xử lý xác nhận check-in cho 1 booking
    //tính toán độ lệch thời gian và áp dụng các luồng nghiệp vụ tương ứng
    CheckInResultResponse confirmCheckIn(Long boookingId);

    /**
     * AC04: Staff thu 20.000đ cọc tại quầy cho khách Walk-in đang bị restricted_until,
     * trước khi có thể gọi confirmCheckIn() cho booking đó.
     */
    CheckInResultResponse collectWalkInPenaltyDeposit(Long bookingId);
}
