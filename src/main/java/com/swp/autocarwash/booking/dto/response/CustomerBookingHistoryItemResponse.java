package com.swp.autocarwash.booking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Chức năng: 1 dòng trong bảng "Lịch sử đặt lịch" của khách hàng, xem bởi Admin
 * trên mọi chi nhánh (FE-US-09-04 AC1) — Mã đặt lịch | Ngày | Loại dịch vụ |
 * Phương tiện | Trạng thái | Số tiền.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerBookingHistoryItemResponse {

    private Long bookingId;

    private LocalDate appointmentDate;

    private String serviceCategoryName;

    private String licensePlate;

    private String brandName;

    /** Trạng thái nội bộ thô của booking (PENDING/CONFIRMED/.../CHECK_OUT). */
    private String status;

    private BigDecimal totalAmount;

    /** Tên nhân viên xử lý (check-in) booking; null nếu chưa có nhân viên check-in. */
    private String staffName;
}
