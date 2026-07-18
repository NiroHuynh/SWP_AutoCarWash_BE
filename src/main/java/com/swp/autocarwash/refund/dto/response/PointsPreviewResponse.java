package com.swp.autocarwash.refund.dto.response;

import lombok.*;

import java.math.BigDecimal;

/**
 * Chức năng: Xem trước số điểm tích lũy sẽ nhận được nếu khách chọn quy đổi tiền cọc
 * thành điểm (refundMethod = LOYALTY_POINTS) — hiển thị TRƯỚC khi khách xác nhận hủy,
 * không tạo Refund, không cộng điểm thật.
 *
 * @author KimNgan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointsPreviewResponse {

    private Long bookingId;

    /** Số tiền cọc sẽ được quy đổi. */
    private BigDecimal depositAmount;

    /** Hạng thành viên hiện tại của khách. */
    private String tierName;

    /** Hệ số nhân điểm của hạng hiện tại. */
    private BigDecimal pointMultiple;

    /** Số điểm sẽ nhận được nếu khách xác nhận quy đổi. */
    private Integer previewPoints;
}
