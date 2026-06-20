package com.swp.autocarwash.booking.calculator;

import com.swp.autocarwash.booking.entity.BookingSlot;

import java.util.Comparator;
import java.util.List;

/**
 *
 * Chức năng:
 * Class SlotAvailabilityCalculator dùng để kiểm tra khả năng đáp ứng lịch đặt dựa trên
 * danh sách các slot thời gian. Class này xác định xem có đủ số lượng slot liên tiếp
 * còn trống để phục vụ một yêu cầu đặt lịch hay không.
 *
 * @author Phong
 * @version 1.0
 */
public class SlotAvailabilityCalculator {

    /**
     *
     * Chức năng:
     * Kiểm tra danh sách booking slot có đủ số lượng slot liên tiếp còn trống
     * để đáp ứng thời lượng yêu cầu đặt lịch hay không.
     *
     * Quy trình:
     * - Sắp xếp các slot theo thời gian bắt đầu.
     * - Kiểm tra sức chứa còn lại của từng slot.
     * - Đếm số lượng slot khả dụng liên tiếp.
     * - Trả về true nếu số slot đạt yêu cầu, ngược lại trả về false.
     *
     * @param slots danh sách các slot cần kiểm tra
     * @param required thời lượng dịch vụ yêu cầu (tính theo phút)
     *
     * @return true nếu đủ slot liên tiếp để đặt lịch, false nếu không đủ
     *
     * @author Phong
     * @version 1.0
     */
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
