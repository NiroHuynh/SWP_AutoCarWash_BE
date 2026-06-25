package com.swp.autocarwash.booking.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * Chức năng: BookingContextResponse dùng để chứa toàn bộ dữ liệu context cần thiết
 * cho quá trình tạo booking, bao gồm thông tin station, khoảng thời gian đặt lịch,
 * phương tiện, gói dịch vụ, addon service và voucher khả dụng.
 *
 * @author Phong
 * @version 1.0
 */
@Data
@Builder
public class BookingContextResponse {

    private Integer stationId;
    private BookingWindowDTO bookingWindow;

    private List<VehicleDTO> vehicles;
    private List<ServicePackageDTO> servicePackages;
    private List<AddonServiceDTO> addonServices;
    private List<VoucherDTO> vouchers;

    /**
     *
     * Chức năng: BookingWindowDTO lưu trữ khoảng thời gian cho phép khách hàng
     * thực hiện đặt lịch booking.
     *
     * @author Phong
     * @version 1.0
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookingWindowDTO {
        private LocalDate minDate;
        private LocalDate maxDate;
    }

    /**
     *
     * Chức năng: VehicleDTO chứa thông tin phương tiện của khách hàng
     * được sử dụng trong quá trình tạo booking.
     *
     * @author Phong
     * @version 1.0
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VehicleDTO {
        private Integer id;
        private String licensePlate;
        private String brandName;
    }

    /**
     *
     * Chức năng: ServicePackageDTO chứa thông tin gói dịch vụ rửa xe,
     * bao gồm tên, giá và thời lượng thực hiện dịch vụ.
     *
     * @author Phong
     * @version 1.0
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ServicePackageDTO {
        private Integer id;
        private String name;
        private BigDecimal basePrice;
        private Integer durationMinutes;
    }

    /**
     *
     * Chức năng: AddonServiceDTO chứa thông tin dịch vụ thêm được khách hàng
     * lựa chọn trong quá trình tạo booking.
     *
     * @author Phong
     * @version 1.0
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddonServiceDTO {
        private Integer id;
        private String name;
        private BigDecimal price;
        private Integer durationMinutes;
    }

    /**
     *
     * Chức năng: VoucherDTO chứa thông tin voucher giảm giá có thể áp dụng
     * cho booking bao gồm mã voucher, phần trăm giảm và điều kiện áp dụng.
     *
     * @author Phong
     * @version 1.0
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VoucherDTO {
        private Integer id;
        private String voucherCode;
        private Integer discountPercentage;
        private BigDecimal minOrderValue;
    }
}
