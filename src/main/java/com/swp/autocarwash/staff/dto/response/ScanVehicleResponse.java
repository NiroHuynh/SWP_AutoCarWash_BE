package com.swp.autocarwash.staff.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
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


    private LocalDate appointmentDate;
    private String bookingType;
    private String brandName;
    private String color;
    private String customerTier;
    private BigDecimal depositAmount;
    private boolean depositPaid;
    private BigDecimal remainingAmount;
    private String serviceName;
    private BigDecimal servicePrice;
    private String stationAddress;
    private String stationName;
    private String status;
    private String technicianName;
    private BigDecimal totalAmount;
    private String voucherCode;
    private BigDecimal voucherDiscountAmount;
    private Integer voucherDiscountPercent;

}
