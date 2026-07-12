package com.swp.autocarwash.promotion.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class UpdatePromotionRequest {
    @NotBlank(message = "Tên chiến dịch không được để trống")
    private String title;

    private String description;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    private LocalDate startDate;

    @NotNull(message = "Ngày kết thúc không được để trống")
    private LocalDate endDate;

    private List<Integer> targetCustomerTierIds;

    @NotEmpty(message = "Danh sách chi nhánh áp dụng không được để trống")
    private List<Integer> stationIds;
}
