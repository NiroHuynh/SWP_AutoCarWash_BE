package com.swp.autocarwash.queue.service;

import com.swp.autocarwash.queue.dto.response.QueueBoardResponse;

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
     * Hủy queue ticket (do khách bỏ về) theo bookingId.
     * booking → CANCELED, slot giải phóng, ticket → CANCELED.
     *
     * @param bookingId    id của booking liên kết với ticket cần hủy
     * @param actingUserId id của staff thực hiện hủy
     * @return QueueBoardResponse — board mới sau khi hủy
     */
    QueueBoardResponse cancelByBookingId(Long bookingId, Long actingUserId);

    /**
     * AC03 — Xe vào làn: booking CHECK_IN→WASHING, ticket→WASHING, 1 làn AVAILABLE→WASHING.
     *
     * @param bookingId id của booking
     * @return QueueBoardResponse — board mới sau khi xe vào làn
     */
    QueueBoardResponse startService(Long bookingId, Integer laneId);

    /**
     * AC01 — Hoàn tất rửa: booking WASHING→COMPLETED, ticket→COMPLETED, 1 làn WASHING→AVAILABLE.
     *
     * @param bookingId id của booking
     * @param laneId    id DB của làn cần giải phóng (FE gửi để tránh giải phóng nhầm)
     * @return QueueBoardResponse — board mới sau khi hoàn tất
     */
    QueueBoardResponse completeService(Long bookingId, Integer laneId);

    /**
     * Staff bật/gỡ bảo trì cho 1 làn của station mình — làn MAINTENANCE không được
     * gán xe mới và không tính vào capacity đặt lịch online của station.
     *
     * @param userId      id user (staff) đang đăng nhập, dùng để tra station
     * @param laneId      id làn cần đổi trạng thái
     * @param maintenance true = bật bảo trì, false = gỡ bảo trì
     * @return QueueBoardResponse — board mới sau khi đổi trạng thái làn
     */
    QueueBoardResponse setLaneMaintenance(Long userId, Integer laneId, boolean maintenance);

}
