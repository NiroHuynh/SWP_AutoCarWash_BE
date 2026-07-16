package com.swp.autocarwash.refund.mapper;

import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.booking.entity.BookingSlotAllocation;
import com.swp.autocarwash.booking.entity.enums.BookingStatus;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.refund.dto.response.RefundDetailResponse;
import com.swp.autocarwash.refund.dto.response.RefundListItemResponse;
import com.swp.autocarwash.refund.dto.response.RefundResponse;
import com.swp.autocarwash.refund.entity.Refund;
import com.swp.autocarwash.station.entity.Station;
import org.springframework.stereotype.Component;

/**
 * Chức năng: Chuyển đổi entity {@link Refund} sang DTO response.
 *
 * @author KimNgan
 * @version 1.0
 */
@Component
public class RefundMapper {

    public RefundResponse toResponse(Refund refund) {
        return RefundResponse.builder()
                .id(refund.getId())
                .bookingId(refund.getBooking().getId())
                .bankName(refund.getRefundBankName())
                .accountNumber(refund.getRefundAccountNumber())
                .accountHolder(refund.getRefundAccountHolder())
                .refundAmount(refund.getRefundAmount())
                .status(refund.getStatus())
                .bookingStatus(BookingStatus.REFUND_PENDING.name())
                .createdAt(refund.getCreatedAt())
                .build();
    }

    public RefundListItemResponse toListItemResponse(Refund refund) {
        Booking booking = refund.getBooking();
        Customer customer = booking.getCustomer();
        return RefundListItemResponse.builder()
                .id(refund.getId())
                .bookingId(booking.getId())
                .customerName(customer != null ? customer.getFullName() : null)
                .customerPhone(customer != null && customer.getUser() != null ? customer.getUser().getPhone() : null)
                .stationName(resolveStationName(booking))
                .refundAmount(refund.getRefundAmount())
                .status(refund.getStatus())
                .createdAt(refund.getCreatedAt())
                .refundedAt(refund.getRefundedAt())
                .build();
    }

    public RefundDetailResponse toDetailResponse(Refund refund, String qrImageUrl) {
        Booking booking = refund.getBooking();
        Customer customer = booking.getCustomer();
        return RefundDetailResponse.builder()
                .id(refund.getId())
                .bookingId(booking.getId())
                .customerName(customer != null ? customer.getFullName() : null)
                .customerPhone(customer != null && customer.getUser() != null ? customer.getUser().getPhone() : null)
                .serviceCategoryName(booking.getServicePackage() != null
                        ? booking.getServicePackage().getServiceCategory().getCategoryName() : null)
                .appointmentDate(booking.getAppointmentDate())
                .refundAmount(refund.getRefundAmount())
                .refundBankName(refund.getRefundBankName())
                .refundAccountNumber(refund.getRefundAccountNumber())
                .refundAccountHolder(refund.getRefundAccountHolder())
                .stationName(resolveStationName(booking))
                .status(refund.getStatus())
                .qrImageUrl(qrImageUrl)
                .refundNote(refund.getRefundNote())
                .refundedAt(refund.getRefundedAt())
                .refundedBy(refund.getRefundedBy())
                .createdAt(refund.getCreatedAt())
                .build();
    }

    private String resolveStationName(Booking booking) {
        return booking.getSlotAllocations().stream()
                .map(BookingSlotAllocation::getBookingSlot)
                .filter(java.util.Objects::nonNull)
                .map(bs -> bs.getStation())
                .filter(java.util.Objects::nonNull)
                .map(Station::getStationName)
                .findFirst()
                .orElse(null);
    }
}
