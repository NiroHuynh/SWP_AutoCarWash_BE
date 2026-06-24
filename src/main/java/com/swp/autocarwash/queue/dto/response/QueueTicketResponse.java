package com.swp.autocarwash.queue.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Chức năng: QueueTicketResponse chứa thông tin 1 ticket trong hàng chờ,
 * dùng để trả về cho FE Queue Dashboard.
 * @author KimNgan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueueTicketResponse {
    /**
     *     licensePlate  = "LMN-4455"
     *     customerTier  = "PLATINUM"
     *     vehicleBrand  = "Audi"
     *     vehicleColor  = "Metallic Grey"
     *     serviceName   = "Premium Wash"
     *     status        = "WAITING"
     */

        private Long id;
        private String ticketNumber;
        private String status;
        private Boolean isBooking;
        private Integer priorityScore;
        private Long bookingId;
        private String licensePlate;
        private String customerName;
        private Integer stationId;
        private String stationName;

}
