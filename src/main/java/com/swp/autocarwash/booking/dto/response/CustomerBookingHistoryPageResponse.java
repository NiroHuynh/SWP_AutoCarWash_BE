package com.swp.autocarwash.booking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Chức năng: Response phân trang cho màn hình "Lịch sử đặt lịch" của khách
 * hàng, xem bởi Admin trên mọi chi nhánh (FE-US-09-04).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerBookingHistoryPageResponse {

    private List<CustomerBookingHistoryItemResponse> content;

    private Integer page;

    private Integer size;

    private Long totalElements;

    private Integer totalPages;
}
