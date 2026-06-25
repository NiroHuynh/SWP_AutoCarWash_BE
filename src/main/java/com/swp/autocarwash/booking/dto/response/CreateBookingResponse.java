package com.swp.autocarwash.booking.dto.response;


import java.math.BigDecimal;
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
    private List<Integer> slotIds;
}