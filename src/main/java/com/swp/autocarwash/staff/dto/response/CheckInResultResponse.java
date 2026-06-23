package com.swp.autocarwash.staff.dto.response;

import com.swp.autocarwash.booking.entity.enums.BookingStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckInResultResponse {
    //Mã booking vừa được xử lí
    private Long bookingId;
    //Trạng thái booking sau khi xử lý(CHECK_IN hoặc NO_SHOW)
    private String status;
    //Mã queueTicket vừa được cấp, null nếu checkin không thành công
    private String queueTicketNumber;
    //Số phút lệch so với giờ hẹn: dương = trễ, âm = sớm, 0.  đúng giờ
    private Integer minutesDeviation;
    // Thông điệp hiển thị cho Staff, ví dụ "Checked in successfully", "Please wait for your time"
    private String message;

     //true nếu nghiệp vụ yêu cầu Frontend chuyển sang màn hình tạo đơn Walk-in mới
     //(trường hợp Booking bị chuyển NO_SHOW và khách là khách lẻ).
    private boolean requiresWalkIn;

     //Mã booking cũ (đã NO_SHOW) cần đính kèm khi Frontend gọi API tạo Walk-in mới,
     //để chuyển 100% deposit_amount sang đơn mới. Null nếu không áp dụng
    private Long oldBookingId;
}
