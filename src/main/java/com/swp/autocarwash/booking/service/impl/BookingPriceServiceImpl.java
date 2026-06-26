package com.swp.autocarwash.booking.service.impl;


import com.swp.autocarwash.auth.util.SecurityUtils;
import com.swp.autocarwash.booking.dto.request.BookingPricePreviewRequest;
import com.swp.autocarwash.booking.dto.response.BookingPricePreviewResponse;
import com.swp.autocarwash.booking.entity.enums.BookingStatus;
import com.swp.autocarwash.booking.port.*;
import com.swp.autocarwash.booking.repository.BookingRepository;
import com.swp.autocarwash.booking.service.BookingPriceService;
import com.swp.autocarwash.booking.validator.BookingPriceValidator;
import com.swp.autocarwash.common.contract.customer.CustomerContract;
import com.swp.autocarwash.common.contract.promotion.VoucherContract;
import com.swp.autocarwash.common.contract.servicepackage.ServicePackageContract;
import com.swp.autocarwash.customer.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;


/**
 *
 * Chức năng: BookingPriceServiceImpl triển khai nghiệp vụ tính toán giá booking
 * trước khi tạo booking. Class này xử lý việc tính giá service package, addon service,
 * kiểm tra voucher và tính tổng tiền cuối cùng.
 *
 * @author Phong
 * @version 1.0
 */

@Service
@RequiredArgsConstructor
public class BookingPriceServiceImpl implements BookingPriceService {

    private final ServicePackagePort servicePackagePort;
    private final AddonServicePort addonServicePort;
    private final VoucherPort voucherPort;
    private final FamilySubscriptionPort familySubscriptionPort;
    private final UnlimitSubscriptionPort unlimitSubscriptionPort;
    private final BookingRepository bookingRepository;
    private final BookingPriceValidator validator;



    /**
     *
     * Chức năng: Tính toán giá preview của booking dựa trên dịch vụ được chọn,
     * addon service và voucher áp dụng.
     *
     * Quy trình:
     * - Validate dữ liệu request trước khi tính giá.
     * - Lấy thông tin service package và lấy giá dịch vụ.
     * - Tính tổng giá addon service được chọn.
     * - Cộng service price và addon price để tạo subtotal.
     * - Kiểm tra voucher code có hợp lệ với giá trị đơn hàng hay không.
     * - Tính số tiền giảm giá nếu voucher hợp lệ.
     * - Tính tổng tiền cuối cùng sau khi giảm giá.
     * - Trả về BookingPricePreviewResponse chứa toàn bộ thông tin giá.
     *
     * @param request thông tin yêu cầu tính giá bao gồm service package,
     *                addon service và voucher code
     *
     * @return BookingPricePreviewResponse chứa chi tiết giá booking
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public BookingPricePreviewResponse calculatePreviewPrice(BookingPricePreviewRequest request) {

        validator.validate(request);

        // 1. Service package price

        BigDecimal servicePrice = getServicePackagePrice(
                request.getVehicleId(), request.getServicePackageId(), LocalDate.parse(request.getAppointmentDate()));

        // 2. Addon price
        BigDecimal addonPrice = getAddonServicePrice(request);

        BigDecimal subTotal = servicePrice.add(addonPrice);

        // 3. Voucher
        Optional<VoucherContract> voucherOpt = voucherPort.getVoucher(request.getVoucherCode(),subTotal);

        BigDecimal discount = BigDecimal.ZERO;
        boolean valid = false;
        Integer percent = null;

        if (voucherOpt.isPresent()) {
            var voucher = voucherOpt.get();

            valid = voucher.isValid(subTotal);
            if (valid) {
                percent = voucher.getDiscountPercentage();
                discount = subTotal.multiply(BigDecimal.valueOf(percent))
                        .divide(BigDecimal.valueOf(100));
            }
        }

        BigDecimal finalTotal = subTotal.subtract(discount);

        boolean isVehicleBookingOnDateAndHasSubscription =
                isVehicleBookingOnDateAndHasSubscription(request.getVehicleId(), request.getServicePackageId(), LocalDate.parse(request.getAppointmentDate()));

        return BookingPricePreviewResponse.builder()
                .currency("VND")
                .isVehicleBookingOnDateAndHasSubscription(isVehicleBookingOnDateAndHasSubscription)
                .breakdown(BookingPricePreviewResponse.PriceBreakdown.builder()
                        .servicePrice(servicePrice)
                        .addonPrice(addonPrice)
                        .subTotal(subTotal)
                        .voucherCode(request.getVoucherCode())
                        .voucherDiscount(discount)
                        .finalTotal(finalTotal)
                        .build())
                .appliedVoucher(BookingPricePreviewResponse.AppliedVoucher.builder()
                        .valid(valid)
                        .discountPercentage(percent)
                        .build())
                .build();
    }




    /**
     * Tính giá tiền của service package
     */
    private BigDecimal getServicePackagePrice(
            Long vehicleId,
            Integer servicePackageId,
            LocalDate appointmentDate
    ) {
        ServicePackageContract servicePackage =
                servicePackagePort.getServicePackage(servicePackageId);

        boolean isVehicleBookingOnDateAndHasSubscription =
                isVehicleBookingOnDateAndHasSubscription(vehicleId, servicePackageId, appointmentDate);

        if (isVehicleBookingOnDateAndHasSubscription) {
            return BigDecimal.ZERO;
        }

        return servicePackage.getBasePrice();
    }

    /**
     * Kiểm tra xe có subscription và đã có booking trong ngày hay chưa
     */
    private boolean isVehicleBookingOnDateAndHasSubscription(
            Long vehicleId,
            Integer servicePackageId,
            LocalDate appointmentDate
    ) {

        boolean hasSubscription =
                hasSubscription(vehicleId, servicePackageId);


        boolean isVehicleBookedOnDate =
                isVehicleBookedOnDate(vehicleId, appointmentDate);


        return hasSubscription && !isVehicleBookedOnDate;
    }

    /**
     * Kiểm tra vehicle đã có booking trong ngày
     */
    private boolean isVehicleBookedOnDate(
            Long vehicleId,
            LocalDate appointmentDate
    ) {

        return bookingRepository
                .existsByVehicleIdAndAppointmentDateAndStatusNot(
                        vehicleId,
                        appointmentDate,
                        BookingStatus.CANCELED.toString()
                );
    }

    private boolean hasSubscription(
            Long vehicleId,
            Integer servicePackageId
    ) {

        boolean hasFamily =
                familySubscriptionPort
                        .hasFamilySubscription(vehicleId, servicePackageId);

        boolean hasUnlimited =
                unlimitSubscriptionPort
                        .hasUnlimitSubscription(vehicleId, servicePackageId);

        return hasFamily || hasUnlimited;
    }

    /**
     * tính giá tiền của addon service
     *
     * @return BigDecimal
     *
     * @Author Phong
     */
    private BigDecimal getAddonServicePrice(BookingPricePreviewRequest request){
        BigDecimal addonPrice;
        boolean haveAddons =  request.getAddonServiceIds()!=null && !request.getAddonServiceIds().isEmpty();

        if(haveAddons){
            addonPrice = addonServicePort.calculateAddonPrice(request.getAddonServiceIds());
        }else{
            addonPrice = BigDecimal.ZERO;
        }

        return addonPrice;
    }
}
