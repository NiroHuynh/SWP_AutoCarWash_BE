package com.swp.autocarwash.booking.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

/**
 *
 * Chức năng: BookingSlotResponse dùng để chứa danh sách các khung giờ booking
 * khả dụng được trả về cho client khi tìm kiếm lịch đặt tại station.
 *
 * @author Phong
 * @version 1.0
 */
@Data
@Builder
public class BookingSlotResponse {


    private List<SlotWindowResponse> slots;
}