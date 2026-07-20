package com.swp.autocarwash.booking.mapper;

import com.swp.autocarwash.booking.dto.response.AddonInfo;
import com.swp.autocarwash.booking.dto.response.BookingCardResponse;
import com.swp.autocarwash.booking.dto.response.BookingDetailResponse;
import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.booking.entity.BookingAddon;
import com.swp.autocarwash.booking.entity.enums.BookingStatus;
import com.swp.autocarwash.common.contract.refund.RefundContract;
import com.swp.autocarwash.station.entity.Station;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

/**
 * Mapper chuyển đổi entity {@link Booking} sang các DTO phản hồi.
 *
 * <p>Thực hiện ánh xạ thủ công (không dùng MapStruct) để giữ sự đơn giản
 * và kiểm soát rõ ràng từng trường được ánh xạ.</p>
 *
 * @author KimNgan
 * @version 1.0
 */
@Component
public class BookingHistoryMapper {

    /** Gom thông tin subscription plan để truyền vào mapper mà không cần nhiều tham số rời. */
    public record SubscriptionInfo(String planName, String planType, Integer durationDays) {}

    /**
     * Chuyển đổi một {@link Booking} cùng thông tin giờ slot và danh sách hành động
     * sang {@link BookingCardResponse}.
     *
     * <p>Dùng chung cho cả tab Upcoming và Past Services. Yêu cầu
     * {@code booking.vehicle} và {@code booking.servicePackage} đã được tải sẵn
     * (eager-fetch) trước khi gọi phương thức này để tránh
     * {@code LazyInitializationException}.</p>
     *
     * @param booking        entity lịch đặt cần chuyển đổi (không được {@code null})
     * @param startTime      giờ bắt đầu lấy từ slot đầu tiên
     * @param endTime        giờ kết thúc lấy từ slot cuối cùng
     * @param allowedActions danh sách hành động được phép
     * @param refund         thông tin hoàn tiền, {@code null} nếu booking chưa có yêu cầu hoàn tiền
     * @return {@link BookingCardResponse} chứa đầy đủ thông tin hiển thị trên booking card
     */
    public BookingCardResponse toBookingCardResponse(
            Booking booking,
            LocalTime startTime,
            LocalTime endTime,
            List<String> allowedActions,
            RefundContract refund) {
// tạo constructor đổ dữ liệu vào = new
        return BookingCardResponse.builder()
                .bookingId(booking.getId())
                .serviceName(booking.getServicePackage().getName())
                .licensePlate(booking.getVehicle().getLicensePlate())
                .brandName(booking.getVehicle().getBrandName())
                .color(booking.getVehicle().getColor())
                .status(booking.getStatus())
                .appointmentDate(booking.getAppointmentDate())
                .startTime(startTime)
                .endTime(endTime)
                .allowedActions(allowedActions)
                .refundAmount(refund != null ? refund.getAmount() : null)
                .refundAccountNumber(refund != null ? refund.getAccountNumber() : null)
                .refundedAt(refund != null ? refund.getRefundedAt() : null)
                .build();
    }

    /**
     * Chuyển đổi một {@link Booking} và các dữ liệu liên quan sang {@link BookingDetailResponse}.
     *
     * @param booking                entity lịch đặt (vehicle, servicePackage, checkInEmployee đã eager-fetch)
     * @param startTime              giờ bắt đầu slot đầu tiên
     * @param endTime                giờ kết thúc slot cuối cùng
     * @param station                chi nhánh lấy từ slot đầu tiên (có thể {@code null})
     * @param addons                 danh sách {@link BookingAddon} đã eager-fetch addonService
     * @param technicianName         họ tên kỹ thuật viên (có thể {@code null})
     * @param voucherCode            mã voucher đã áp dụng (có thể {@code null})
     * @param voucherDiscountPercent phần trăm giảm giá voucher (có thể {@code null})
     * @param remainingAmount        số tiền còn lại sau khi trừ cọc
     * @param refund                 thông tin hoàn tiền, {@code null} nếu booking chưa có yêu cầu hoàn tiền
     * @return {@link BookingDetailResponse} hoàn chỉnh
     */
    public BookingDetailResponse toBookingDetailResponse(
            Booking booking,
            LocalTime startTime,
            LocalTime endTime,
            Station station,
            List<BookingAddon> addons,
            String technicianName,
            String voucherCode,
            Integer voucherDiscountPercent,
            BigDecimal depositAmount,
            BigDecimal remainingAmount,
            SubscriptionInfo subscriptionInfo,
            Integer loyaltyPoint,
            Integer pointsEarned,
            Integer pointsRedeemed,
            RefundContract refund) {

        List<AddonInfo> addonInfos = addons.stream()
                .map(ba -> AddonInfo.builder()
                        .addonName(ba.getAddonService().getName())
                        .addonPrice(ba.getPrice())
                        .build())
                .toList();

        return BookingDetailResponse.builder()
                .bookingId(booking.getId())
                .customerName(booking.getCustomer() != null
                        ? booking.getCustomer().getFirstName() + " " + booking.getCustomer().getLastName()
                        : null)
                .status(booking.getStatus())
                .bookingType(booking.getBookingType())
                .customerTier(booking.getCustomer() != null && booking.getCustomer().getCustomerTier() != null
                        ? booking.getCustomer().getCustomerTier().getTierName() : null)
                .subscriptionPlanName(subscriptionInfo != null ? subscriptionInfo.planName() : null)
                .subscriptionPlanType(subscriptionInfo != null ? subscriptionInfo.planType() : null)
                .subscriptionDurationDays(subscriptionInfo != null ? subscriptionInfo.durationDays() : null)
                .serviceCategoryName(booking.getServicePackage().getServiceCategory().getCategoryName())
                .serviceName(booking.getServicePackage().getName())
                .addons(addonInfos)
                .licensePlate(booking.getVehicle().getLicensePlate())
                .brandName(booking.getVehicle().getBrandName())
                .color(booking.getVehicle().getColor())
                .stationName(station != null ? station.getStationName() : null)
                .stationAddress(station != null ? station.getAddress() : null)
                .appointmentDate(booking.getAppointmentDate())
                .startTime(startTime)
                .endTime(endTime)
                .checkInAt(booking.getCheckInAt())
                .checkOutAt(booking.getCheckOutAt())
                .technicianName(technicianName)
                .servicePrice(booking.getTotalServiceAmount())
                .addonTotal(booking.getTotalAddonAmount())
                .voucherCode(voucherCode)
                .voucherDiscountPercent(voucherDiscountPercent)
                .voucherDiscountAmount(booking.getVoucherDiscountAmount())
                .pointDiscountAmount(booking.getPointDiscountAmount())
                .discountAmount(
                        (booking.getVoucherDiscountAmount() != null
                                ? booking.getVoucherDiscountAmount() : BigDecimal.ZERO)
                        .add(booking.getPointDiscountAmount() != null
                                ? booking.getPointDiscountAmount() : BigDecimal.ZERO))
                .totalAmount(booking.getTotalAmount())
                .isDepositPaid(booking.getIsDepositPaid())
                .depositAmount(depositAmount)
                .remainingAmount(remainingAmount)
                .loyaltyPoint(loyaltyPoint)
                .pointsEarned(pointsEarned)
                .pointsRedeemed(pointsRedeemed)
                .refundBankName(refund != null ? refund.getBankName() : null)
                .refundAccountNumber(refund != null ? refund.getAccountNumber() : null)
                .refundAmount(refund != null ? refund.getAmount() : null)
                .refundedAt(refund != null ? refund.getRefundedAt() : null)
                .build();
    }
}
