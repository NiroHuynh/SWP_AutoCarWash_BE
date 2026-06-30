package com.swp.autocarwash.booking.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * DTO phản hồi dùng chung cho booking card, hiển thị trên cả tab
 * "Upcoming Appointments" (AC-25.1.x) và "Past Services" (AC-25.2.x).
 *
 * <p>Giá trị hợp lệ của {@code status} và {@code allowedActions} phụ thuộc
 * vào nguồn gọi: {@code CONFIRMED}/{@code CHECK_IN}/{@code WASHING} với
 * action {@code CANCEL}/{@code VIEW_DETAILS} cho tab Upcoming; {@code PAID}/
 * {@code CANCELED}/{@code NO_SHOW} với action {@code WRITE_REVIEW} cho tab
 * Past. Việc map {@code status} sang nhãn hiển thị và màu badge do FE đảm
 * nhiệm, BE chỉ trả giá trị trạng thái thô.</p>
 *
 * @author KimNgan
 * @version 1.0
 */
@Getter
@Builder
public class BookingCardResponse {

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
     * Trạng thái nội bộ của lịch đặt.
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
     * Danh sách hành động được phép thực hiện trên booking card này.
     */
    private List<String> allowedActions;
}
