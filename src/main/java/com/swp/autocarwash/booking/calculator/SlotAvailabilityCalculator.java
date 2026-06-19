package com.swp.autocarwash.booking.calculator;

import com.swp.autocarwash.booking.entity.BookingSlot;

import java.util.Comparator;
import java.util.List;

/**
 * SlotAvaiabilityCalculator dùng để kiểm tra xem có đủ số lượng slot liên tiếp để đặt lịch hay không.
 *
 * @Author Phong
 * @Version 1.0
 */
public class SlotAvailabilityCalculator {
    public boolean validateContinuousSlots(List<BookingSlot> slots, int required) {

        // 1. sort theo time
        slots.sort(Comparator.comparing(BookingSlot::getStartTime));

        int count = 0;

        for (BookingSlot slot : slots) {

            int available = slot.getMaxCapacity() - slot.getBookedCount();

            if (available <= 0) {
                return false;
            }

            count++;
            // required là số phút, mỗi slot là 15 phút, nên chia cho 15 để ra số slot cần thiết
            if (count >= required/15) {
                return true;
            }
        }

        return false;
    }
}
