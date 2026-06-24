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
     * Chức năng: Lấy danh sách ticket đang active (WAITING/IN_SERVICE) trên mọi station.
     *
     * @return danh sách ticket đang active, sắp theo độ ưu tiên
     */
    List<QueueTicketResponse> getActiveQueue();
}
