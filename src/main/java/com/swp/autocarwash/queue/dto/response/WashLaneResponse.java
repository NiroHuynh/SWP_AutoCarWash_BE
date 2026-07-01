package com.swp.autocarwash.queue.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Chức năng: Thông tin 1 làn rửa đang hoạt động (chưa bị xoá) của chi nhánh,
 * dùng cho FE Queue Dashboard render từng ô làn theo trạng thái thật.
 *
 * @author KimNgan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WashLaneResponse {
    private Integer id;
    private String laneName;
    private String status;
    private Long currentBookingId;
}
