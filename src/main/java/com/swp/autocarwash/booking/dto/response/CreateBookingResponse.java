package com.swp.autocarwash.booking.dto.response;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * Chức năng: CreateBookingResponse dùng để chứa thông tin kết quả sau khi tạo booking thành công,
 * bao gồm mã booking, trạng thái booking, tổng số tiền và danh sách slot đã được đặt.
 *
 * @author Phong
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingResponse {

    private Long bookingId;
    private String status;
    private BigDecimal totalAmount;
    private List<Long> slotIds;

    /** Số tiền cọc khách cần chuyển khoản để xác nhận booking. */
    private BigDecimal depositAmount;

    /** Nội dung chuyển khoản để webhook SePay map booking, dạng "BK{bookingId}". */
    private String transferContent;

    /** URL ảnh QR VietQR đã điền sẵn số tài khoản/số tiền/nội dung, FE chỉ cần {@code <img src>}. */
    private String qrImageUrl;

    /** Thời điểm QR/cọc hết hạn = createdAt + PENDING_PAYMENT_TIMEOUT_MINUTES — FE dùng để hiển thị đồng hồ đếm ngược. */
    private LocalDateTime expiresAt;
}