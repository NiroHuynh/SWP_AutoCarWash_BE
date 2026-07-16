package com.swp.autocarwash.booking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Chức năng: Response phân trang cho màn hình "Danh sách booking" của 1 chi
 * nhánh, xem bởi Staff/Admin.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StationBookingListPageResponse {

    private List<StationBookingListItemResponse> content;

    private Integer page;

    private Integer size;

    private Long totalElements;

    private Integer totalPages;
}
