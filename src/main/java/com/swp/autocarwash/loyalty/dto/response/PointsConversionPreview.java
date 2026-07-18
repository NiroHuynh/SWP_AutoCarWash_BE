package com.swp.autocarwash.loyalty.dto.response;

import lombok.*;

import java.math.BigDecimal;

/**
 * Chức năng: Kết quả tính trước (không cộng điểm thật) số điểm một khách hàng sẽ nhận
 * được nếu quy đổi 1 khoản tiền theo hạng hiện tại của họ.
 *
 * @author KimNgan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointsConversionPreview {

    private String tierName;

    private BigDecimal pointMultiple;

    private Integer previewPoints;
}
