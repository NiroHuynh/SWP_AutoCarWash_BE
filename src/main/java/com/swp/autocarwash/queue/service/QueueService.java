package com.swp.autocarwash.queue.service;

import com.swp.autocarwash.queue.dto.response.QueueBoardResponse;
import com.swp.autocarwash.queue.dto.response.QueueTicketResponse;

import java.util.List;

/**
 * Chức năng: Nghiệp vụ truy vấn hàng chờ cho Queue Dashboard.
 *
 * @author KimNgan
 * @version 1.0
 */
public interface QueueService {
    /**
     * Chức năng: Lấy danh sách ticket (trừ CANCELED) thuộc station của staff đang đăng nhập.
     *
     * @param userId id của user (staff) đang đăng nhập, dùng để tra station
     * @return danh sách ticket, sắp theo độ ưu tiên
     */
    QueueBoardResponse getActiveQueue(Long userId);

    /**
     * Chức năng: Hủy queue ticket (do khách bỏ về) theo ticketId.
     * Nếu ticket có booking, hủy booking và giải phóng slot; nếu walk-in chỉ hủy ticket.
     *
     * @param ticketId id của queue ticket cần hủy
     * @param actingUserId id của staff thực hiện hủy
     * @return QueueTicketResponse với status=CANCELED
     */
    QueueTicketResponse cancelByTicketId(Long ticketId, Long actingUserId);

    /**
     * AC03 — Xác nhận xe vào làn: ticket WAITING→IN_SERVICE, booking→WASHING, 1 làn AVAILABLE→OCCUPIED.
     *
     * @param ticketId id của queue ticket
     * @return QueueTicketResponse với status=IN_SERVICE
     */
    QueueTicketResponse startService(Long ticketId);

    /**
     * AC01 — Hoàn tất rửa: ticket IN_SERVICE→COMPLETED, booking WASHING→COMPLETED, 1 làn OCCUPIED→AVAILABLE.
     *
     * @param ticketId id của queue ticket
     * @return QueueTicketResponse với status=COMPLETED
     */
    QueueTicketResponse completeService(Long ticketId);

}
