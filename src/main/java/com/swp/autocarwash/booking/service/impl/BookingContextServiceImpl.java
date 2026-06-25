package com.swp.autocarwash.booking.service.impl;

import com.swp.autocarwash.booking.dto.response.BookingContextResponse;
import com.swp.autocarwash.booking.mapper.BookingMapper;
import com.swp.autocarwash.booking.port.*;
import com.swp.autocarwash.booking.service.BookingContextService;
import com.swp.autocarwash.common.contract.customer.VehicleContract;
import com.swp.autocarwash.common.contract.loyalty.CustomerTierContract;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * Chức năng: BookingContextServiceImpl triển khai nghiệp vụ xây dựng dữ liệu context
 * phục vụ quá trình tạo booking. Class này tổng hợp thông tin customer, vehicle,
 * service package, addon service và voucher để cung cấp dữ liệu cho màn hình Schedule.
 *
 * @author Phong
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class BookingContextServiceImpl implements BookingContextService {

    private final VehiclePort vehiclePort;
    private final ServicePackagePort servicePackagePort;
    private final AddonServicePort addonServicePort;
    private final VoucherPort voucherPort;
    private final BookingMapper bookingMapper;
    private final CustomerPort customerPort;

    /**
     *
     * Chức năng: Lấy customerId hiện tại đang thực hiện thao tác booking.
     *
     * Quy trình:
     * - Lấy thông tin user hiện tại từ JWT hoặc session.
     * - Mapping user sang customerId tương ứng.
     * - Trả về customerId phục vụ các nghiệp vụ booking.
     *
     * @return id của customer hiện tại
     *
     * @author Phong
     * @version 1.0
     */
    private Integer getCurrentCustomerId() {

        // CASE 1: JWT
        // return jwtUtil.getUserId();

        // CASE 2: SESSION
        // return (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return 1; // mock
    }

    /**
     *
     * Chức năng: Xây dựng toàn bộ dữ liệu context cần thiết cho màn hình tạo booking.
     *
     * Quy trình:
     * - Lấy customerId hiện tại.
     * - Lấy danh sách vehicle thuộc customer.
     * - Kiểm tra customer đã đăng ký vehicle hay chưa.
     * - Tính toán booking window dựa trên tier của customer.
     * - Lấy danh sách service package, addon service và voucher khả dụng.
     * - Mapping dữ liệu sang BookingContextResponse.
     * - Trả về dữ liệu context hoàn chỉnh.
     *
     * @param stationId id của station cần lấy dữ liệu booking context
     *
     * @return BookingContextResponse chứa toàn bộ dữ liệu phục vụ tạo booking
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public BookingContextResponse getBookingContext(Integer stationId) {

        Integer customerId = getCurrentCustomerId();


        // AC: compute booking window theo tier
        LocalDate now = LocalDate.now();
        int limitDays = resolveTierLimitDays(customerId);

        BookingContextResponse.BookingWindowDTO window =
                BookingContextResponse.BookingWindowDTO.builder()
                        .minDate(now)
                        .maxDate(now.plusDays(limitDays))
                        .build();

        return BookingContextResponse.builder()
                .stationId(stationId)
                .bookingWindow(window)
                .servicePackages(servicePackagePort.getAllPackages()
                        .stream().map(bookingMapper::toPackage).toList())
                .addonServices(addonServicePort.getAllAddons()
                        .stream().map(bookingMapper::toAddon).toList())
                .vouchers(voucherPort.getValidVouchers(customerId)
                        .stream().map(bookingMapper::toVoucher).toList())
                .build();
    }

    /**
     *
     * Chức năng: Xác định số ngày tối đa khách hàng được phép đặt lịch trước
     * dựa trên tier hiện tại.
     *
     * Quy trình:
     * - Lấy thông tin tier của customer thông qua CustomerPort.
     * - Đọc cấu hình booking window từ tier.
     * - Trả về số ngày được phép đặt trước.
     *
     * @param customerId id của customer cần lấy thông tin tier
     *
     * @return số ngày tối đa customer được phép đặt lịch trước
     *
     * @author Phong
     * @version 1.0
     */
    private int resolveTierLimitDays(Integer customerId) {

        CustomerTierContract tier = customerPort.getTierOfCustomer(customerId);
        return tier.getBookingWindowDays();

    }
}
