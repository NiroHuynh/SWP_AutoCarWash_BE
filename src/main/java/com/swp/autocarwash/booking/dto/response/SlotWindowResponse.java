package com.swp.autocarwash.booking.dto.response;


import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

/**
 *
 * Chức năng: SlotWindowResponse dùng để chứa thông tin của một khoảng thời gian booking,
 * bao gồm danh sách slot tương ứng, thời gian bắt đầu, thời gian kết thúc và trạng thái
 * khả dụng của khoảng thời gian đó.
 *
 * @author Phong
 * @version 1.0
 */
@Data
@Builder
public class SlotWindowResponse {

    private List<Integer> slotIds;

    private LocalTime startTime;

    private LocalTime endTime;

    private boolean available;
}
