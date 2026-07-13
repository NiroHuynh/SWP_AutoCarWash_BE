package com.swp.autocarwash.refund.dto.response;

import lombok.*;

import java.util.List;

/**
 * Chức năng: Response tổng của màn hình "Danh sách hoàn tiền" — số liệu tổng hợp (AC9)
 * + bảng phân trang (AC1, AC6-AC8, AC10).
 *
 * @author KimNgan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundListPageResponse {

    private RefundSummaryResponse summary;
    private List<RefundListItemResponse> content;
    private Integer page;
    private Integer size;
    private Long totalElements;
    private Integer totalPages;
}
