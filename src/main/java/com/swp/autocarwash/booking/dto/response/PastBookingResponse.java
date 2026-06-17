package com.swp.autocarwash.booking.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * DTO phản hồi cho từng lịch sử dịch vụ hiển thị trên tab "Past Services".
 *
 * <p>Chứa đủ thông tin để render một booking card theo AC-25.2.3,
 * nhãn trạng thái theo AC-25.2.4, và danh sách hành động theo AC-25.2.3.</p>
 *
 * @author KimNgan
 * @version 1.0
 */
@Getter
@Builder
public class PastBookingResponse {

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
     * Nhận một trong: {@code PAID}, {@code CANCELLED}, {@code NO_SHOW}.
     */
    private String status;

    /**
     * Nhãn trạng thái hiển thị cho người dùng theo AC-25.2.4:
     * <ul>
     *   <li>{@code PAID}      → "Đã thanh toán"</li>
     *   <li>{@code CANCELLED} → "Đã hủy"</li>
     *   <li>{@code NO_SHOW}   → "Vắng mặt"</li>
     * </ul>
     */
    private String statusLabel;

    /**
     * Mã màu hex cho badge trạng thái theo AC-25.2.4:
     * <ul>
     *   <li>{@code PAID}      → "#22C55E" (xanh lá)</li>
     *   <li>{@code CANCELLED} → "#9CA3AF" (xám)</li>
     *   <li>{@code NO_SHOW}   → "#F97316" (cam)</li>
     * </ul>
     */
    private String statusColor;

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
     * Danh sách hành động được phép trên booking card theo AC-25.2.3:
     * <ul>
     *   <li>{@code PAID}      → {@code ["WRITE_REVIEW"]}</li>
     *   <li>{@code CANCELLED} → {@code []}</li>
     *   <li>{@code NO_SHOW}   → {@code []}</li>
     * </ul>
     */
    private List<String> allowedActions;
}
