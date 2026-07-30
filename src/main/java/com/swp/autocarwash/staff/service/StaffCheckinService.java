package com.swp.autocarwash.staff.service;

import com.swp.autocarwash.staff.dto.response.CheckInResultResponse;
import com.swp.autocarwash.staff.dto.response.ScanVehicleResponse;

public interface StaffCheckinService {
    //xử lí logic khi staff quét biển số xe tại quầy
    ScanVehicleResponse scanVehicle(String licensePlate);
    //Xử lý xác nhận check-in cho 1 booking
    //tính toán độ lệch thời gian và áp dụng các luồng nghiệp vụ tương ứng
    CheckInResultResponse confirmCheckIn(Long boookingId);

}
