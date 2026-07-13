package com.swp.autocarwash.promotion.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PromotionDashboardListViewResponse {

    private Integer id;
    private String type; // "CAMPAIGN" hoặc "STANDALONE_VOUCHER"
    private String name;
    private List<String> appliedStations; // Danh sách tên chi nhánh áp dụng
    private List<String> targetSegments;  // Danh sách tên nhóm đối tượng thụ hưởng
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;

    private Integer configMode;
    private Long voucherId;
    private String voucherCode;
}
