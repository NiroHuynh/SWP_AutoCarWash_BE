package com.swp.autocarwash.booking.service.impl;

import com.swp.autocarwash.auth.util.SecurityUtils;
import com.swp.autocarwash.booking.dto.response.BookingContextResponse;
import com.swp.autocarwash.booking.mapper.BookingMapper;
import com.swp.autocarwash.booking.port.*;
import com.swp.autocarwash.booking.service.BookingContextService;
import com.swp.autocarwash.common.contract.customer.CustomerContract;
import com.swp.autocarwash.common.contract.customer.VehicleContract;
import com.swp.autocarwash.common.contract.loyalty.CustomerTierContract;
import com.swp.autocarwash.common.contract.station.StationContract;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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
    private final StationPort stationPort;
    private final FamilySubscriptionPort familySubscriptionPort;
    private final UnlimitSubscriptionPort unlimitSubscriptionPort;
    private final FamilyGroupPort familyGroupPort;

    private final SecurityUtils securityUtils;

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
    private Long getCurrentUserId() {
        return securityUtils.getCurrentUserId();
//        return 1L;
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

        Long userId = getCurrentUserId();
        CustomerContract customer = customerPort.getCustomerByUserId(userId);

        List<VehicleContract> vehicles = vehiclePort.getVehiclesByCustomer(customer.getId());

        if (vehicles == null || vehicles.isEmpty()) {
            throw new BusinessException(ErrorCode.NO_VEHICLE_REGISTERED);
        }

        List<BookingContextResponse.VehicleDTO> vehicleDTOS = getSubscriptionOfVehicles(vehicles);

        // AC: compute booking window theo tier
        LocalDate now = LocalDate.now();
        int limitDays = resolveTierLimitDays(customer.getId());

        BookingContextResponse.BookingWindowDTO window =
                BookingContextResponse.BookingWindowDTO.builder()
                        .minDate(now)
                        .maxDate(now.plusDays(limitDays))
                        .build();

        StationContract station = stationPort.getStationById(stationId);

        return BookingContextResponse.builder()
                .station(bookingMapper.toStationDTO(station))
                .bookingWindow(window)
                .vehicles(vehicleDTOS)
                .servicePackages(servicePackagePort.getAllPackages()
                        .stream().map(bookingMapper::toPackage).toList())
                .addonServices(addonServicePort.getAllAddons()
                        .stream().map(bookingMapper::toAddon).toList())
                .vouchers(voucherPort.getValidVouchers(customer.getId())
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
    private int resolveTierLimitDays(Long customerId) {
        Long ownerCustomerId = familyGroupPort.getOwnerCustomerIdOfCustomerId(customerId);
        Integer bookingWindowDay = customerPort.getTierOfCustomer(customerId).getBookingWindowDays();
        if(ownerCustomerId!=null && ownerCustomerId != customerId){
            CustomerTierContract ownerTier = customerPort.getTierOfCustomer(ownerCustomerId);
            if(bookingWindowDay<ownerTier.getBookingWindowDays()){
                bookingWindowDay = ownerTier.getBookingWindowDays();
            }
        }
        return bookingWindowDay;
    }

    /**
     *
     * chức năng: dùng để lấy các gói unlimit, family của vehicle nếu có
     *
     * @author Phong
     * @version 1.0
     */
    private List<BookingContextResponse.VehicleDTO> getSubscriptionOfVehicles(List<VehicleContract> vehicleContracts){
        List<BookingContextResponse.VehicleDTO> vehicleDTOS = new ArrayList<>();
        for(VehicleContract vehicleContract : vehicleContracts){
            BookingContextResponse.VehicleDTO vehicleDTO = bookingMapper.toVehicleDTO(vehicleContract);
            BookingContextResponse.VehicleDTO.ActiveSubscription activeSubscription = getActiveSubscription(vehicleContract);
            vehicleDTO.setActiveSubscription(
                activeSubscription
            );
            vehicleDTOS.add(vehicleDTO);
        }
        return vehicleDTOS;
    }


    /**
     *
     * chức năng: tìm kiếm xem xe có gói unlimit, family hay không để set cho ActionSubscription
     *
     * @author Phong
     * @version 1.0
     */
    private BookingContextResponse.VehicleDTO.ActiveSubscription getActiveSubscription(VehicleContract vehicleContract){
        Integer servicePackageId = null;
        String type = null;

        Integer familyPackageId = familySubscriptionPort.getActiveServicePackageId(vehicleContract.getId());
        Integer unlimitPackageId = unlimitSubscriptionPort.getActiveServicePackageId(vehicleContract.getId());
        if(familyPackageId!=null){
            type = "FAMILY";
            servicePackageId=familyPackageId;
        } else if (unlimitPackageId != null) {
            type = "UNLIMITTED";
            servicePackageId=unlimitPackageId;
        }

        if(unlimitPackageId!=null || familyPackageId!=null){
            return BookingContextResponse.VehicleDTO.ActiveSubscription.builder()
                    .servicePackageId(servicePackageId)
                    .type(type)
                    .build();
        }

        return null;

    }
}
