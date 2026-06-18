package com.swp.autocarwash.booking.validator;

import com.swp.autocarwash.booking.entity.BookingSlot;

import java.util.List;

public class BookingSlotValidator {

    /**
     * Validate consecutive slot availability
     */
    public static boolean isValid(List<BookingSlot> slots) {

        if (slots == null || slots.isEmpty()) return false;

        int requiredCapacity = 1;

        for (BookingSlot slot : slots) {
            int available = slot.getMaxCapacity() - slot.getBookedCount();

            if (available < requiredCapacity) {
                return false;
            }
        }

        return true;
    }
}