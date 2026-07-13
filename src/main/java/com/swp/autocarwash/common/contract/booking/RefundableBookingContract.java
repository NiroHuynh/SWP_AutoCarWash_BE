package com.swp.autocarwash.common.contract.booking;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Chức năng: Snapshot dữ liệu của một Booking cần cho luồng hoàn tiền (Refund),
 * truyền qua ranh giới module booking → refund mà không lộ JPA entity.
 *
 * <p>Ownership (booking thuộc customer nào) đã được kiểm tra tại phía booking khi
 * tạo contract này; refund chỉ dùng các field bên dưới để validate điều kiện hoàn tiền
 * và snapshot số tiền cọc.</p>
 *
 * @author KimNgan
 * @version 1.0
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefundableBookingContract {

    private Long bookingId;

    /** Trạng thái hiện tại của booking (String, xem BookingStatus). */
    private String status;

    /** Đã trả cọc online hay chưa. */
    private Boolean isDepositPaid;

    /** Ngày hẹn. */
    private LocalDate appointmentDate;

    /** Giờ bắt đầu của slot sớm nhất được cấp cho booking (null nếu chưa có slot). */
    private LocalTime slotStartTime;

    /** Số tiền cọc cố định toàn hệ thống (snapshot cứng vào Refund). */
    private BigDecimal depositAmount;
}
