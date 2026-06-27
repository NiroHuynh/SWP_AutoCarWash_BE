package com.swp.autocarwash.queue.service;

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
     * Chức năng: Lấy danh sách ticket (trừ CANCELLED) thuộc station của staff đang đăng nhập.
     *
     * @param userId id của user (staff) đang đăng nhập, dùng để tra station
     * @return danh sách ticket, sắp theo độ ưu tiên
     */
    List<QueueTicketResponse> getActiveQueue(Long userId);

    /**
     * Chức năng: Hủy queue ticket (do khách bỏ về) theo ticketId.
     * Nếu ticket có booking, hủy booking và giải phóng slot; nếu walk-in chỉ hủy ticket.
     *
     * @param ticketId id của queue ticket cần hủy
     * @param actingUserId id của staff thực hiện hủy
     * @return QueueTicketResponse với status=CANCELLED
     */
    QueueTicketResponse cancelByTicketId(Long ticketId, Long actingUserId);

    /**
     * Chức năng: Chuyển queue ticket từ WAITING sang IN_SERVICE (thêm xe vào làn rửa).
     * Nếu ticket có booking, đồng thời đổi booking sang WASHING.
     *
     * @param ticketId id của queue ticket cần chuyển sang IN_SERVICE
     * @return QueueTicketResponse với status=IN_SERVICE
     */
    QueueTicketResponse startService(Long ticketId);
}
