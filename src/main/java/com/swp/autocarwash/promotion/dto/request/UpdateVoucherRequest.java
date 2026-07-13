package com.swp.autocarwash.promotion.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateVoucherRequest {

    @NotBlank(message = "Mã voucher không được để trống")
    private String voucherCode;

    private Integer discountPercentage;

    private BigDecimal maxDiscountAmount;

    @NotNull(message = "Giá trị đơn hàng tối thiểu không được để trống")
    @Min(value = 0, message = "Giá trị đơn hàng tối thiểu phải từ 0 trở lên")
    private BigDecimal minOrderValue;

    @NotNull(message = "Tổng số lượt sử dụng không được để trống")
    private Integer usageLimit;

    @NotNull(message = "Thời gian bắt đầu không được để trống")
    private LocalDateTime startDate;

    @NotNull(message = "Thời gian hết hạn không được để trống")
    private LocalDateTime expiryDate;

    private Boolean reusable;
}
