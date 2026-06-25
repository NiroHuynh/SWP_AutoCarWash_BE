package com.swp.autocarwash.staff.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateWalkInResponse {

        /** id của Booking vừa được tạo. */
        private Long bookingId;

        /** id của QueueTicket vừa được phát hành cho Booking này. */
        private Long queueTicketId;

        /** Số thứ tự hiển thị cho khách, ví dụ "W015" (W = Walk-in). */
        private String ticketNumber;

        /** Trạng thái Booking sau khi tạo - luôn là "CHECKED_IN" theo đúng AC05. */
        private String status;

        /** Số tiền khách còn phải trả lúc lấy xe (copy lại từ bước calculate-invoice, để Staff đối chiếu lần cuối). */
        private BigDecimal remainingBalance;

        private String message;
}
