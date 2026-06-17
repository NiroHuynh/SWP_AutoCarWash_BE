package com.swp.autocarwash.booking.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * DTO phản hồi cho từng lịch đặt sắp tới hiển thị trên tab "Upcoming Appointments".
 *
 * <p>Chứa đủ thông tin để render một booking card theo AC-25.1.3
 * và danh sách hành động được phép theo AC-25.1.4, AC-25.1.5, AC-25.1.6.</p>
 *
 * @author KimNgan
 * @version 1.0
 */
@Getter
@Builder
public class UpcomingBookingResponse {

    /**
     * Mã định danh của lịch đặt.
     */
    private Long bookingId;

    /**
     * Tên gói dịch vụ chính (ví dụ: "Normal Wash", "Premium Wash").
     */
    private String serviceName;

    /**
     * Biển số xe (ví dụ: "51A-11111").
     */
    private String licensePlate;

    /**
     * Hãng xe (ví dụ: "Toyota", "Honda").
     */
    private String brandName;

    /**
     * Màu sắc xe (ví dụ: "White", "Black").
     */
    private String color;

    /**
     * Trạng thái hiện tại của lịch đặt.
     * Chỉ nhận một trong các giá trị: {@code CONFIRMED}, {@code CHECKED_IN}, {@code WASHING}.
     */
    private String status;

    /**
     * Ngày hẹn rửa xe.
     */
    private LocalDate appointmentDate;

    /**
     * Giờ bắt đầu dự kiến (lấy từ slot đầu tiên được phân bổ).
     */
    private LocalTime startTime;

    /**
     * Giờ kết thúc dự kiến (lấy từ slot cuối cùng được phân bổ).
     */
    private LocalTime endTime;

    /**
     * Danh sách hành động mà customer được phép thực hiện trên booking card này.
     *
     * <p>Quy tắc (AC-25.1.4 / AC-25.1.5 / AC-25.1.6):
     * <ul>
     *   <li>{@code CONFIRMED} + còn ≥ 120 phút đến giờ hẹn →
     *       {@code ["CANCEL", "VIEW_DETAILS"]}</li>
     *   <li>{@code CONFIRMED} + còn &lt; 120 phút đến giờ hẹn →
     *       {@code ["VIEW_DETAILS"]}</li>
     *   <li>{@code CHECKED_IN} hoặc {@code WASHING} →
     *       {@code ["VIEW_DETAILS"]}</li>
     * </ul>
     * Nút {@code WRITE_REVIEW} không xuất hiện ở tab Upcoming — chỉ hiển thị
     * ở tab Past Services sau khi booking hoàn thành.</p>
     */
    private List<String> allowedActions;
}
