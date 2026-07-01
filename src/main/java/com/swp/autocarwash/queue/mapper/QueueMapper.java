package com.swp.autocarwash.queue.mapper;

import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.entity.Vehicle;
import com.swp.autocarwash.queue.dto.response.QueueTicketResponse;
import com.swp.autocarwash.queue.entity.QueueTicket;
import org.springframework.stereotype.Component;

/**
 * Chức năng: Chuyển đổi QueueTicket entity sang QueueTicketResponse cho tầng API.
 *
 * @author KimNgan
 * @version 1.0
 */
@Component
public class QueueMapper {

    /**
     * Chức năng: Map 1 QueueTicket sang QueueTicketResponse, lấy kèm thông tin
     * vehicle/customer từ booking liên kết (nếu có).
     * @param ticket entity cần chuyển đổi
     * @return response tương ứng để trả cho FE
     */
    public QueueTicketResponse toResponse(QueueTicket ticket) {
        Booking booking = ticket.getBooking();
        Vehicle vehicle = booking != null ? booking.getVehicle() : null;
        Customer customer = booking != null ? booking.getCustomer() : null;

        return QueueTicketResponse.builder()
                .id(ticket.getId())
                .ticketNumber(ticket.getTicketNumber())
                .status(booking != null ? booking.getStatus() : ticket.getStatus())
                .isBooking(ticket.getIsBooking())
                .priorityScore(ticket.getPriorityScore())
                .bookingId(booking != null ? booking.getId() : null)
                .licensePlate(vehicle != null ? vehicle.getLicensePlate() : null)
                .customerName(customer != null ? customer.getFirstName() + " " + customer.getLastName() : null)
                .customerTier(customer != null && customer.getCustomerTier() != null ? customer.getCustomerTier().getTierName() : null)
                .vehicleBrand(vehicle != null ? vehicle.getBrandName() : null)
                .vehicleColor(vehicle != null ? vehicle.getColor() : null)
                .serviceName(booking != null && booking.getServicePackage() != null ? booking.getServicePackage().getName() : null)
                .stationId(ticket.getStation() != null ? ticket.getStation().getId() : null)
                .stationName(ticket.getStation() != null ? ticket.getStation().getStationName() : null)
                .laneId(ticket.getWashLane() != null ? ticket.getWashLane().getId() : null)
                .build();
    }
}