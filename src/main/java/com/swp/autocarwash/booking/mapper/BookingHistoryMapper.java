package com.swp.autocarwash.booking.mapper;

import com.swp.autocarwash.booking.dto.response.AddonInfo;
import com.swp.autocarwash.booking.dto.response.BookingCardResponse;
import com.swp.autocarwash.booking.dto.response.BookingDetailResponse;
import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.booking.entity.BookingAddon;
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
     * @return {@link BookingCardResponse} chứa đầy đủ thông tin hiển thị trên booking card
     */
    public BookingCardResponse toBookingCardResponse(
            Booking booking,
            LocalTime startTime,
            LocalTime endTime,
            List<String> allowedActions) {
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
            SubscriptionInfo subscriptionInfo) {

        List<AddonInfo> addonInfos = addons.stream()
                .map(ba -> AddonInfo.builder()
                        .addonName(ba.getAddonService().getName())
                        .addonPrice(ba.getPrice())
                        .build())
                .toList();

        return BookingDetailResponse.builder()
                .bookingId(booking.getId())
                .status(booking.getStatus())
                .bookingType(booking.getBookingType())
                .customerTier(booking.getCustomer() != null && booking.getCustomer().getCustomerTier() != null
                        ? booking.getCustomer().getCustomerTier().getTierName() : null)
                .subscriptionPlanName(subscriptionInfo != null ? subscriptionInfo.planName() : null)
                .subscriptionPlanType(subscriptionInfo != null ? subscriptionInfo.planType() : null)
                .subscriptionDurationDays(subscriptionInfo != null ? subscriptionInfo.durationDays() : null)
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
                .technicianName(technicianName)
                .servicePrice(booking.getTotalServiceAmount())
                .addonTotal(booking.getTotalAddonAmount())
                .voucherCode(voucherCode)
                .voucherDiscountPercent(voucherDiscountPercent)
                .voucherDiscountAmount(booking.getVoucherDiscountAmount())
                .totalAmount(booking.getTotalAmount())
                .isDepositPaid(booking.getIsDepositPaid())
                .depositAmount(depositAmount)
                .remainingAmount(remainingAmount)
                .build();
    }
}
