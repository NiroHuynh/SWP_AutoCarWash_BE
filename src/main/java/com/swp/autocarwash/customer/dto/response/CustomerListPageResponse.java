package com.swp.autocarwash.customer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Chức năng: Response tổng của màn hình Quản lý khách hàng (FE-US-09) — 3 thẻ
 * KPI + bảng phân trang khách hàng đã từng hoàn thành lượt rửa.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerListPageResponse {

    private CustomerSummaryResponse summary;

    private List<CustomerListItemResponse> content;

    private Integer page;

    private Integer size;

    private Long totalElements;

    private Integer totalPages;
}
