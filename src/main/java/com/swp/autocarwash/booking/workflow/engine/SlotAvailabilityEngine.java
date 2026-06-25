package com.swp.autocarwash.booking.workflow.engine;

import com.swp.autocarwash.booking.dto.response.SlotWindowResponse;
import com.swp.autocarwash.booking.entity.BookingSlot;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Chức năng: SlotAvailabilityEngine xử lý logic kiểm tra và xây dựng các khoảng thời gian
 * booking khả dụng dựa trên danh sách slot, yêu cầu thời lượng và điều kiện nghiệp vụ.
 *
 * Class này chịu trách nhiệm kiểm tra slot liên tục theo thời gian, đảm bảo slot còn
 * capacity và tạo ra các window mà khách hàng có thể lựa chọn để đặt lịch.
 *
 * @author Phong
 * @version 1.0
 */
@Component
public class SlotAvailabilityEngine {

    private static final int SLOT_DURATION = 15;

    /**
     *
     * Chức năng: Xây dựng danh sách các khoảng thời gian booking khả dụng từ danh sách slot.
     *
     * Quy trình:
     * - Duyệt qua danh sách slot theo từng vị trí.
     * - Tạo window gồm số lượng slot cần thiết theo requiredSlots.
     * - Kiểm tra các slot trong window có liên tục về thời gian hay không.
     * - Kiểm tra tất cả slot trong window còn capacity để đặt lịch.
     * - Tạo SlotWindowResponse nếu window hợp lệ.
     * - Trả về danh sách các khoảng thời gian có thể booking.
     *
     * @param slots danh sách slot của station trong ngày cần kiểm tra
     * @param requiredSlots số lượng slot liên tiếp cần thiết cho booking
     *
     * @return danh sách SlotWindowResponse chứa các khoảng thời gian có thể đặt
     *
     * @author Phong
     * @version 1.0
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
     *
     * Chức năng: Kiểm tra các slot trong một window có liên tục về mặt thời gian hay không.
     *
     * Quy trình:
     * - Duyệt từng cặp slot liền kề trong window.
     * - So sánh thời gian kết thúc của slot trước với thời gian bắt đầu của slot sau.
     * - Nếu có khoảng cách thời gian thì window không hợp lệ.
     * - Nếu tất cả slot nối tiếp nhau thì trả về true.
     *
     * @param window danh sách slot cần kiểm tra tính liên tục
     *
     * @return true nếu các slot liên tục nhau, false nếu bị gián đoạn
     *
     * @author Phong
     * @version 1.0
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
     *
     * Chức năng: Kiểm tra tất cả slot trong một window còn đủ capacity để booking.
     *
     * Quy trình:
     * - Duyệt qua từng slot trong window.
     * - Tính số lượng slot còn trống bằng maxCapacity - bookedCount.
     * - Kiểm tra tất cả slot đều còn khả năng tiếp nhận booking.
     * - Trả về kết quả kiểm tra.
     *
     * @param window danh sách slot cần kiểm tra capacity
     *
     * @return true nếu tất cả slot còn capacity, false nếu có slot đã đầy
     *
     * @author Phong
     * @version 1.0
     */
    private boolean hasCapacity(List<BookingSlot> window) {
        return window.stream()
                .allMatch(s -> (s.getMaxCapacity() - s.getBookedCount()) > 0);
    }
}
