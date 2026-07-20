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
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.repository.CustomerRepository;
import com.swp.autocarwash.promotion.entity.Promotion;
import com.swp.autocarwash.promotion.entity.PromotionStationMapping;
import com.swp.autocarwash.promotion.entity.Voucher;
import com.swp.autocarwash.promotion.entity.enums.VoucherStatus;
import com.swp.autocarwash.promotion.repository.PromotionRepository;
import com.swp.autocarwash.promotion.repository.PromotionStationMappingRepository;
import com.swp.autocarwash.promotion.repository.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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

    private final CustomerRepository customerRepository;
    private final PromotionRepository promotionRepository;
    private final VoucherRepository voucherRepository;
    private final PromotionStationMappingRepository promotionStationMappingRepository;
    private final SecurityUtils securityUtils;

    @Override
    public BookingPricePreviewResponse calculatePreviewPrice(BookingPricePreviewRequest request) {

        validator.validate(request);

        // 1. Tính giá gốc Service Package và Addon Service
        BigDecimal servicePrice = getServicePackagePrice(
                request.getVehicleId(), request.getServicePackageId(), LocalDate.parse(request.getAppointmentDate()));

        BigDecimal addonPrice = getAddonServicePrice(request);

        // BƯỚC 1: Kiểm tra xem xe có sở hữu gói cước (Unlimited/Family) còn hạn trong ngày này không
        boolean isVehicleBookingOnDateAndHasSubscription =
                isVehicleBookingOnDateAndHasSubscription(request.getVehicleId(), request.getServicePackageId(), LocalDate.parse(request.getAppointmentDate()));

        // BƯỚC 2: TÍNH TOÁN SONG SONG HAI MỐC GIÁ ĐỂ ĐỐI SOÁT
        // Mốc A: Tổng giá trị hóa đơn gốc (Gói + Addon) dùng để check điều kiện minOrderValue
        BigDecimal totalOriginalPrice = servicePrice.add(addonPrice);

        // Mốc B: SubTotal thực tế khách phải trả (Nếu có gói cước thì gói = 0đ, chỉ tính tiền Addon) dùng để tính % giảm trừ
        BigDecimal actualServicePrice = isVehicleBookingOnDateAndHasSubscription ? BigDecimal.ZERO : servicePrice;
        BigDecimal subTotal = actualServicePrice.add(addonPrice);

        LocalDate appDate = LocalDate.parse(request.getAppointmentDate());
        LocalDateTime appDateTime = appDate.atStartOfDay();

        // 2. Lấy thông tin khách hàng và Hạng thành viên (Rank)
        Long userId = getCurrentUserId();
        Customer customer = customerRepository.findByUserId(userId);
        if (customer == null) {
            throw new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND);
        }
        Integer customerTierId = customer.getCustomerTier().getId();

        // Khởi tạo các biến chứa kết quả Voucher sẽ áp dụng
        BigDecimal discountAmount = BigDecimal.ZERO;
        Voucher appliedVoucher = null;
        boolean valid = false;
        Integer percent = null;
        String finalAppliedCode = null;

        // =========================================================================
        // HỆ THỐNG GÁC CỔNG VOUCHER ĐA CHẾ ĐỘ (ĐÃ SỬA ĐẢO QUYỀN ƯU TIÊN VÀ FIX CHECK MIN VALUE)
        // =========================================================================

        //ƯU TIÊN 1: Khách tự nhập/chọn mã -> Phải xử lý độc lập trước để tránh bị kẹt tiền mã cũ
        if (request.getVoucherCode() != null && !request.getVoucherCode().trim().isEmpty()) {
            String inputCode = request.getVoucherCode().trim().toUpperCase();

            Voucher voucher = voucherRepository.findByVoucherCodeAndIsDeletedFalse(inputCode)
                    .orElseThrow(() -> new BusinessException(ErrorCode.VOUCHER_NOT_FOUND));

            if (!"ACTIVE".equalsIgnoreCase(voucher.getStatus())) {
                throw new BusinessException(ErrorCode.VOUCHER_NOT_FOUND);
            }

            if (appDateTime.isBefore(voucher.getStartDate()) || appDateTime.isAfter(voucher.getExpiryDate())) {
                throw new BusinessException(ErrorCode.VOUCHER_EXPIRED);
            }

            if (voucher.getUsedCount() >= voucher.getUsageLimit()) {
                throw new BusinessException(ErrorCode.VOUCHER_OUT_OF_STOCK);
            }

            if (voucher.getPromotion() != null) {
                List<PromotionStationMapping> mappings = promotionStationMappingRepository.findById_PromotionId(voucher.getPromotion().getId());
                boolean isStationValid = mappings.stream().anyMatch(m -> m.getId().getStationId().equals(request.getStationId()));
                if (!isStationValid) {
                    throw new BusinessException(ErrorCode.VOUCHER_STATION_INVALID);
                }
            }

            //FIX BUG 1: Dùng tổng giá trị đơn hàng GỐC để check điều kiện minOrderValue của Voucher
            if (totalOriginalPrice.compareTo(voucher.getMinOrderValue()) < 0) {
                throw new BusinessException(ErrorCode.VOUCHER_NOT_APPLICABLE);
            }

            appliedVoucher = voucher;
            finalAppliedCode = voucher.getVoucherCode();
            valid = true;
        }
        //ƯU TIÊN 2: Khách không chọn mã nào -> Hệ thống mới tự động quét Chế độ 1 cho họ
        else {
            List<Promotion> activePromos = promotionRepository.findActiveDirectPromotionsForUser(request.getStationId(), appDate, customerTierId);

            if (activePromos != null && !activePromos.isEmpty()) {
                BigDecimal maxDiscountCalculated = BigDecimal.ZERO;
                Voucher bestVoucher = null;

                for (Promotion p : activePromos) {
                    List<Voucher> subVouchers = voucherRepository.findByPromotionId(p.getId());

                    for (Voucher tempVoucher : subVouchers) {
                        if (tempVoucher == null || !VoucherStatus.ACTIVE.name().equalsIgnoreCase(tempVoucher.getStatus()) || Boolean.TRUE.equals(tempVoucher.getIsDeleted())) {
                            continue;
                        }

                        // Tự động quét cũng dựa trên tổng giá gốc để đảm bảo quyền lợi
                        boolean isMinOrderValid = totalOriginalPrice.compareTo(tempVoucher.getMinOrderValue()) >= 0;
                        if (!isMinOrderValid) {
                            continue;
                        }

                        BigDecimal currentDiscount = BigDecimal.ZERO;
                        if (tempVoucher.getDiscountPercentage() != null) {
                            BigDecimal percentFactor = BigDecimal.valueOf(tempVoucher.getDiscountPercentage())
                                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                            // Tính toán số tiền được giảm dựa trên SubTotal thực tế khách phải trả (tiền addon)
                            BigDecimal calculated = subTotal.multiply(percentFactor);
                            currentDiscount = (tempVoucher.getMaxDiscountAmount() != null && calculated.compareTo(tempVoucher.getMaxDiscountAmount()) > 0)
                                    ? tempVoucher.getMaxDiscountAmount() : calculated;
                        } else {
                            currentDiscount = tempVoucher.getMaxDiscountAmount();
                        }

                        if (currentDiscount.compareTo(maxDiscountCalculated) > 0 || bestVoucher == null) {
                            maxDiscountCalculated = currentDiscount;
                            bestVoucher = tempVoucher;
                        }
                    }
                }
                if (bestVoucher != null) {
                    appliedVoucher = bestVoucher;
                    finalAppliedCode = bestVoucher.getVoucherCode();
                    valid = true;
                }
            }
        }

        // =========================================================================
        // LOGIC TÍNH KHẤU TRỪ TIỀN GIẢM GIÁ THỰC TẾ (Tính trên subTotal thực trả)
        // =========================================================================
        if (appliedVoucher != null && valid) {
            percent = appliedVoucher.getDiscountPercentage();
            if (percent != null) {
                BigDecimal percentFactor = BigDecimal.valueOf(percent).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                BigDecimal calculatedDiscount = subTotal.multiply(percentFactor); // Giảm dựa trên tiền addon 100k

                discountAmount = (appliedVoucher.getMaxDiscountAmount() != null && calculatedDiscount.compareTo(appliedVoucher.getMaxDiscountAmount()) > 0)
                        ? appliedVoucher.getMaxDiscountAmount() : calculatedDiscount;
            } else {
                discountAmount = appliedVoucher.getMaxDiscountAmount();
            }

            if (discountAmount.compareTo(subTotal) > 0) {
                discountAmount = subTotal;
            }
        }

        // BƯỚC 3: Tổng tiền thanh toán cuối cùng = subTotal thực tế trừ đi tiền giảm giá từ Voucher
        BigDecimal finalTotal = subTotal.subtract(discountAmount);

        // 3. Đóng gói dữ liệu trả về cho Frontend
        return BookingPricePreviewResponse.builder()
                .currency("VND")
                .isVehicleBookingOnDateAndHasSubscription(isVehicleBookingOnDateAndHasSubscription)
                .breakdown(BookingPricePreviewResponse.PriceBreakdown.builder()
                        .servicePrice(servicePrice)
                        .addonPrice(addonPrice)
                        .subTotal(isVehicleBookingOnDateAndHasSubscription ? addonPrice : totalOriginalPrice)
                        .voucherCode(finalAppliedCode)
                        .voucherDiscount(discountAmount)
                        .finalTotal(finalTotal)
                        .build())
                .appliedVoucher(BookingPricePreviewResponse.AppliedVoucher.builder()
                        .valid(valid)
                        .discountPercentage(percent)
                        .build())
                .build();
    }

    //VietBinh v1
//    @Override
//    public BookingPricePreviewResponse calculatePreviewPrice(BookingPricePreviewRequest request) {
//
//        validator.validate(request);
//
//        // 1. Tính giá gốc Service Package và Addon Service y như cũ
//        BigDecimal servicePrice = getServicePackagePrice(
//                request.getVehicleId(), request.getServicePackageId(), LocalDate.parse(request.getAppointmentDate()));
//
//        BigDecimal addonPrice = getAddonServicePrice(request);
//        BigDecimal subTotal = servicePrice.add(addonPrice);
//
//        LocalDate appDate = LocalDate.parse(request.getAppointmentDate());
//        LocalDateTime appDateTime = appDate.atStartOfDay();
//
//        // 2. Lấy thông tin khách hàng và Hạng thành viên (Rank) để đối soát Chế độ 1
////        Long userId = SecurityUtils.getCurrentUserId(); // Hoặc hàm lấy userId nhóm em đang dùng
//        Long userId = getCurrentUserId();
//
//        Customer customer = customerRepository.findByUserId(userId);
//        if (customer == null) {
//            throw new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND);
//        }
//        Integer customerTierId = customer.getCustomerTier().getId();
//
//        // Khởi tạo các biến chứa kết quả Voucher sẽ áp dụng
//        BigDecimal discountAmount = BigDecimal.ZERO;
//        Voucher appliedVoucher = null;
//        boolean valid = false;
//        Integer percent = null;
//        String finalAppliedCode = null;
//
//        // =========================================================================
//        // HỆ THỐNG GÁC CỔNG VOUCHER ĐA CHẾ ĐỘ (ĐỒNG BỘ TỪ LUỒNG CREATE BOOKING)
//        // =========================================================================
//
//        // CONFIG 1: Tự động quét Chế độ 1 (Giảm giá sàn trực tiếp theo Chi nhánh + Hạng thành viên)
//        List<Promotion> activePromos = promotionRepository.findActiveDirectPromotionsForUser(request.getStationId(), appDate, customerTierId);
//
//        if (activePromos != null && !activePromos.isEmpty()) {
//            BigDecimal maxDiscountCalculated = BigDecimal.ZERO;
//            Voucher bestVoucher = null;
//
//            for (Promotion p : activePromos) {
//                List<Voucher> subVouchers = voucherRepository.findByPromotionId(p.getId());
//
//                for (Voucher tempVoucher : subVouchers) {
//                    if (tempVoucher == null || !VoucherStatus.ACTIVE.name().equalsIgnoreCase(tempVoucher.getStatus()) || Boolean.TRUE.equals(tempVoucher.getIsDeleted())) {
//                        continue;
//                    }
//
//                    BigDecimal currentDiscount = BigDecimal.ZERO;
//                    if (tempVoucher.getDiscountPercentage() != null) {
//                        BigDecimal percentFactor = BigDecimal.valueOf(tempVoucher.getDiscountPercentage())
//                                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
//                        BigDecimal calculated = subTotal.multiply(percentFactor);
//                        currentDiscount = (tempVoucher.getMaxDiscountAmount() != null && calculated.compareTo(tempVoucher.getMaxDiscountAmount()) > 0)
//                                ? tempVoucher.getMaxDiscountAmount() : calculated;
//                    } else {
//                        currentDiscount = tempVoucher.getMaxDiscountAmount();
//                    }
//
//                    if (currentDiscount.compareTo(maxDiscountCalculated) > 0 || bestVoucher == null) {
//                        maxDiscountCalculated = currentDiscount;
//                        bestVoucher = tempVoucher;
//                    }
//                }
//            }
//            if (bestVoucher != null) {
//                appliedVoucher = bestVoucher;
//                finalAppliedCode = bestVoucher.getVoucherCode(); // Lấy mã tự động (Ví dụ: AUTO_PROMO_12) để hiển thị lên invoice
//                valid = true;
//            }
//        }
//        // CONFIG 2 & 3: Nếu không dính Chế độ 1, kiểm tra mã Voucher khách tự chọn/tự nhập
//        else if (request.getVoucherCode() != null && !request.getVoucherCode().trim().isEmpty()) {
//            String inputCode = request.getVoucherCode().trim().toUpperCase();
//
//            // Tìm voucher, nếu khách nhập sai mã thì luồng preview chỉ coi như không áp dụng (hoặc quăng lỗi)
//            //Voucher voucher = voucherRepository.findByVoucherCodeAndIsDeletedFalse(inputCode).orElse(null);
//
//            Voucher voucher = voucherRepository.findByVoucherCodeAndIsDeletedFalse(inputCode)
//                    .orElseThrow(() -> new BusinessException(ErrorCode.VOUCHER_NOT_FOUND));
//
//            if (voucher != null) {
//                // Thực hiện chuỗi chốt chặn kiểm tra tính hợp lệ
//                boolean isStatusValid = "ACTIVE".equalsIgnoreCase(voucher.getStatus()) && !Boolean.TRUE.equals(voucher.getIsDeleted());
//                boolean isTimeValid = !appDateTime.isBefore(voucher.getStartDate()) && !appDateTime.isAfter(voucher.getExpiryDate());
//                boolean isUsageValid = voucher.getUsedCount() < voucher.getUsageLimit();
//                boolean isMinOrderValid = subTotal.compareTo(voucher.getMinOrderValue()) >= 0;
//
//                boolean isStationValid = true;
//                if (voucher.getPromotion() != null) {
//                    List<PromotionStationMapping> mappings = promotionStationMappingRepository.findById_PromotionId(voucher.getPromotion().getId());
//                    isStationValid = mappings.stream().anyMatch(m -> m.getId().getStationId().equals(request.getStationId()));
//                }
//
//                // Nếu vượt qua toàn bộ chốt chặn thì công nhận mã hợp lệ để tính giá tạm tính
//                if (isStatusValid && isTimeValid && isUsageValid && isMinOrderValid && isStationValid) {
//                    appliedVoucher = voucher;
//                    finalAppliedCode = voucher.getVoucherCode();
//                    valid = true;
//                }
//            }
//        }
//
//        // =========================================================================
//        // LOGIC TÍNH KHẤU TRỪ TIỀN GIẢM GIÁ THỰC TẾ
//        // =========================================================================
//        if (appliedVoucher != null) {
//            percent = appliedVoucher.getDiscountPercentage();
//            if (percent != null) {
//                BigDecimal percentFactor = BigDecimal.valueOf(percent).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
//                BigDecimal calculatedDiscount = subTotal.multiply(percentFactor);
//
//                discountAmount = (appliedVoucher.getMaxDiscountAmount() != null && calculatedDiscount.compareTo(appliedVoucher.getMaxDiscountAmount()) > 0)
//                        ? appliedVoucher.getMaxDiscountAmount() : calculatedDiscount;
//            } else {
//                discountAmount = appliedVoucher.getMaxDiscountAmount();
//            }
//
//            if (discountAmount.compareTo(subTotal) > 0) {
//                discountAmount = subTotal;
//            }
//        }
//
//        BigDecimal finalTotal = subTotal.subtract(discountAmount);
//
//        boolean isVehicleBookingOnDateAndHasSubscription =
//                isVehicleBookingOnDateAndHasSubscription(request.getVehicleId(), request.getServicePackageId(), LocalDate.parse(request.getAppointmentDate()));
//
//        // 3. Đóng gói dữ liệu trả về chính xác cho hóa đơn tạm tính của Frontend
//        return BookingPricePreviewResponse.builder()
//                .currency("VND")
//                .isVehicleBookingOnDateAndHasSubscription(isVehicleBookingOnDateAndHasSubscription)
//                .breakdown(BookingPricePreviewResponse.PriceBreakdown.builder()
//                        .servicePrice(servicePrice)
//                        .addonPrice(addonPrice)
//                        .subTotal(subTotal)
//                        .voucherCode(finalAppliedCode) // Trả ra tên mã (Dù là mã AUTO hay mã nhập tay) để FE hiển thị lên dòng Voucher của hóa đơn
//                        .voucherDiscount(discountAmount) // Dòng số tiền được giảm trừ
//                        .finalTotal(finalTotal) // Tổng tiền cuối cùng khách phải trả
//                        .build())
//                .appliedVoucher(BookingPricePreviewResponse.AppliedVoucher.builder()
//                        .valid(valid)
//                        .discountPercentage(percent)
//                        .build())
//                .build();
//    }

    /**
     *
     * Chức năng: Lấy customerId hiện tại đang thực hiện thao tác booking.
     * <p>
     * Quy trình:
     * - Lấy thông tin user hiện tại từ JWT hoặc session.
     * - Mapping user sang customerId tương ứng.
     * - Trả về customerId phục vụ các nghiệp vụ booking.
     *
     * @return id của customer hiện tại
     */
    private Long getCurrentUserId() {
        return securityUtils.getCurrentUserId();
//        return 1L;
    }

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
//    @Override
//    public BookingPricePreviewResponse calculatePreviewPrice(BookingPricePreviewRequest request) {
//
//        validator.validate(request);
//
//        // 1. Service package price
//
//        BigDecimal servicePrice = getServicePackagePrice(
//                request.getVehicleId(), request.getServicePackageId(), LocalDate.parse(request.getAppointmentDate()));
//
//        // 2. Addon price
//        BigDecimal addonPrice = getAddonServicePrice(request);
//
//        BigDecimal subTotal = servicePrice.add(addonPrice);
//
//        // 3. Voucher
//        Optional<VoucherContract> voucherOpt = voucherPort.getVoucher(request.getVoucherCode(),subTotal);
//
//        BigDecimal discount = BigDecimal.ZERO;
//        boolean valid = false;
//        Integer percent = null;
//
//        if (voucherOpt.isPresent()) {
//            var voucher = voucherOpt.get();
//
//            valid = voucher.isValid(subTotal);
//            if (valid) {
//                percent = voucher.getDiscountPercentage();
//                discount = subTotal.multiply(BigDecimal.valueOf(percent))
//                        .divide(BigDecimal.valueOf(100));
//            }
//        }
//
//        BigDecimal finalTotal = subTotal.subtract(discount);
//
//        boolean isVehicleBookingOnDateAndHasSubscription =
//                isVehicleBookingOnDateAndHasSubscription(request.getVehicleId(), request.getServicePackageId(), LocalDate.parse(request.getAppointmentDate()));
//
//        return BookingPricePreviewResponse.builder()
//                .currency("VND")
//                .isVehicleBookingOnDateAndHasSubscription(isVehicleBookingOnDateAndHasSubscription)
//                .breakdown(BookingPricePreviewResponse.PriceBreakdown.builder()
//                        .servicePrice(servicePrice)
//                        .addonPrice(addonPrice)
//                        .subTotal(subTotal)
//                        .voucherCode(request.getVoucherCode())
//                        .voucherDiscount(discount)
//                        .finalTotal(finalTotal)
//                        .build())
//                .appliedVoucher(BookingPricePreviewResponse.AppliedVoucher.builder()
//                        .valid(valid)
//                        .discountPercentage(percent)
//                        .build())
//                .build();
//    }




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
