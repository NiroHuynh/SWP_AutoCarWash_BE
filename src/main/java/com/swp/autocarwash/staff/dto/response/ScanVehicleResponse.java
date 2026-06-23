package com.swp.autocarwash.staff.dto.response;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScanVehicleResponse {

    //Mã đặt lịch
    private Long bookingId;
    //Biển số xe được quét
    private String licensePlate;
    //Họ tên khách hàng
    private String customerName;
    //Giờ bắt đầu -> khung giờ đã đặt
    private LocalTime slotStartTime;
    //Giờ kết thúc -> khung giờ đã đặt
    private LocalTime slotEndTime;
    //check thử booking này có tồn tại không
    //true nếu tìm thấy booking CONFIRMED, false nếu là khách vãng lai
    private boolean hasBooking;
    //true nếu xe đang bị khoá đặt lịch (restricted_until > thời điểm hiện tại)
    //UI cần để hiện thị thông tin cảnh báo này
    private boolean isVehiclePenalized;

}
