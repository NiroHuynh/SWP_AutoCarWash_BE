package com.swp.autocarwash.booking.calculator;

import com.swp.autocarwash.booking.entity.BookingSlot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
@Component
@RequiredArgsConstructor
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
     * @param requiredMinutes thời lượng dịch vụ yêu cầu (tính theo phút)
     *
     * @return true nếu đủ slot liên tiếp để đặt lịch, false nếu không đủ
     *
     * @author Phong
     * @version 1.0
     */
    public boolean validateContinuousSlots(
            List<BookingSlot> slots,
            int requiredMinutes
    ) {

        // mỗi slot = 15 phút
        int requiredSlotCount = (int) Math.ceil(
                (double) requiredMinutes / 15
        );


        // user chọn dư slot
        if (slots.size() != requiredSlotCount) {
            return false;
        }


        // sort theo thời gian
        slots.sort(
                Comparator.comparing(
                        BookingSlot::getStartTime
                )
        );


        for (int i = 0; i < slots.size(); i++) {

            BookingSlot current = slots.get(i);


            // check capacity
            int available =
                    current.getMaxCapacity()
                            - current.getBookedCount();


            if (available <= 0) {
                return false;
            }


            // từ slot thứ 2 trở đi kiểm tra liên tục
            if (i > 0) {

                BookingSlot previous =
                        slots.get(i - 1);


                if (!isContinuous(
                        previous,
                        current
                )) {
                    return false;
                }
            }
        }


        return true;
    }

    private boolean isContinuous(
            BookingSlot previous,
            BookingSlot current
    ) {

        return previous.getEndTime()
                .equals(current.getStartTime());
    }
}
