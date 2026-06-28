package com.swp.autocarwash.queue.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Chức năng: Payload cho Queue Dashboard — danh sách hàng chờ + số làn rửa đang trống.
 *
 * <p>{@code availableLaneCount > 0} là tín hiệu để FE hiển thị nút
 * [Confirm vehicle has entered lane] trên xe ở vị trí #1 (AC02).</p>
 *
 * @author KimNgan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueueBoardResponse {
    private long availableLaneCount;
    private List<QueueTicketResponse> queue; // đã sắp xếp: priorityScore DESC, issuedAt ASC
}