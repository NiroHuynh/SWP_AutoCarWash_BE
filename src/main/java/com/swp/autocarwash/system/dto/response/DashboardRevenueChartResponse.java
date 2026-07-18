package com.swp.autocarwash.system.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardRevenueChartResponse {

    private BigDecimal revenueTotal;

    private List<RevenueChartItemResponse> chartData;

}
