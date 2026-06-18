package com.swp.autocarwash.booking.workflow.engine;

import com.swp.autocarwash.booking.dto.response.SlotWindowResponse;
import com.swp.autocarwash.booking.entity.BookingSlot;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * SlotAvailabilityEngine dùng để kiểm tra và tính toán các khung giờ có sẵn để đặt lịch hay không
 *
 * @author Phong
 * @version 1.0
 */

@Component
public class SlotAvailabilityEngine {

    private static final int SLOT_DURATION = 15;

    /**
     * core AC-06: generate available windows
     */
    public List<SlotWindowResponse> buildWindows(
            List<BookingSlot> slots,
            int requiredSlots
    ) {

        List<SlotWindowResponse> result = new ArrayList<>();

        for (int i = 0; i <= slots.size() - requiredSlots; i++) {

            List<BookingSlot> window = slots.subList(i, i + requiredSlots);

            if (isContinuous(window) && hasCapacity(window)) {

                result.add(SlotWindowResponse.builder()
                        .slotIds(window.stream().map(BookingSlot::getId).toList())
                        .startTime(window.get(0).getStartTime())
                        .endTime(window.get(window.size() - 1).getEndTime())
                        .available(true)
                        .build());
            }
        }

        return result;
    }

    /**
     * check 15min continuity
     */
    private boolean isContinuous(List<BookingSlot> window) {
        for (int i = 1; i < window.size(); i++) {
            if (!window.get(i - 1).getEndTime().equals(window.get(i).getStartTime())) {
                return false;
            }
        }
        return true;
    }

    /**
     * AC rule: capacity must exist on all slots
     */
    private boolean hasCapacity(List<BookingSlot> window) {
        return window.stream()
                .allMatch(s -> (s.getMaxCapacity() - s.getBookedCount()) > 0);
    }
}
