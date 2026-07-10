package com.swp.autocarwash.booking.service.impl;

import com.swp.autocarwash.auth.util.SecurityUtils;
import com.swp.autocarwash.booking.dto.response.BookingContextResponse;
import com.swp.autocarwash.booking.mapper.BookingMapper;
import com.swp.autocarwash.booking.port.*;
import com.swp.autocarwash.booking.repository.BookingRepository;
import com.swp.autocarwash.booking.service.BookingContextService;
import com.swp.autocarwash.common.contract.customer.CustomerContract;
import com.swp.autocarwash.common.contract.customer.VehicleContract;
import com.swp.autocarwash.common.contract.loyalty.CustomerTierContract;
import com.swp.autocarwash.common.contract.station.StationContract;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.entity.Vehicle;
import com.swp.autocarwash.customer.repository.CustomerRepository;
import com.swp.autocarwash.customer.repository.VehicleRepository;
import com.swp.autocarwash.promotion.entity.Promotion;
import com.swp.autocarwash.promotion.entity.Voucher;
import com.swp.autocarwash.promotion.repository.PromotionRepository;
import com.swp.autocarwash.promotion.repository.VoucherRepository;
import com.swp.autocarwash.servicepackage.repository.AddonServiceRepository;
import com.swp.autocarwash.servicepackage.repository.ServicePackageRepository;
import com.swp.autocarwash.station.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private final BookingRepository bookingRepository;

    private final SecurityUtils securityUtils;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private ServicePackageRepository servicePackageRepository;

    @Autowired
    private AddonServiceRepository addonServiceRepository;

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private PromotionRepository promotionRepository;


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
//    @Override
//    public BookingContextResponse getBookingContext(Integer stationId) {
//
//        Long userId = getCurrentUserId();
//        CustomerContract customer = customerPort.getCustomerByUserId(userId);
//
//        List<VehicleContract> vehicles = vehiclePort.getVehiclesByCustomer(customer.getId());
//
//        if (vehicles == null || vehicles.isEmpty()) {
//            throw new BusinessException(ErrorCode.NO_VEHICLE_REGISTERED);
//        }
//
//        // AC: compute booking window theo tier
//        LocalDate now = LocalDate.now();
//        int limitDays = resolveTierLimitDays(customer.getId());
//
//        BookingContextResponse.BookingWindowDTO window =
//                BookingContextResponse.BookingWindowDTO.builder()
//                        .minDate(now)
//                        .maxDate(now.plusDays(limitDays))
//                        .build();
//
//        List<BookingContextResponse.VehicleDTO> vehicleDTOS = getSubscriptionOfVehicles(vehicles,window);
//
//        StationContract station = stationPort.getStationById(stationId);
//
//        return BookingContextResponse.builder()
//                .station(bookingMapper.toStationDTO(station))
//                .bookingWindow(window)
//                .vehicles(vehicleDTOS)
//                .servicePackages(servicePackagePort.getAllPackages())
//                .addonServices(addonServicePort.getAllAddons()
//                        .stream().map(bookingMapper::toAddon).toList())
//                .vouchers(voucherPort.getValidVouchers(customer.getId())
//                        .stream().map(bookingMapper::toVoucher).toList())
//                .build();
//    }


    //Hàm này sẽ tự động check xem Chi nhánh có đang chạy giảm giá sàn (Chế độ 1) không. Nếu có -> Ẩn danh sách Voucher khác để tránh giảm giá chồng. Nếu không -> Trả về danh sách Voucher hợp lệ (Chế độ 2 và Chế độ 3 Public).

    @Override
    public BookingContextResponse getBookingContext(Integer stationId) {

        Long userId = getCurrentUserId();

        Customer customer = customerRepository.findByUserId(userId);
        if(customer == null){
            throw new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND);
        }

        //Customer có chứa thông tin hạng thành viên (ví dụ: customer.getTierId() hoặc customer.getPromotionTarget().getId())
        Integer customerTierId = customer.getCustomerTier().getId();

        List<VehicleContract> vehicles = vehiclePort.getVehiclesByCustomer(customer.getId());

        if (vehicles == null || vehicles.isEmpty()) {
            throw new BusinessException(ErrorCode.NO_VEHICLE_REGISTERED);
        }

        // AC: compute booking window theo tier
        LocalDate now = LocalDate.now();
        int limitDays = resolveTierLimitDays(customer.getId());

        BookingContextResponse.BookingWindowDTO window =
                BookingContextResponse.BookingWindowDTO.builder()
                        .minDate(now)
                        .maxDate(now.plusDays(limitDays))
                        .build();

        List<BookingContextResponse.VehicleDTO> vehicleDTOS = getSubscriptionOfVehicles(vehicles,window);

        StationContract station = stationPort.getStationById(stationId);


        //XỬ LÝ VOUCHER

        LocalDateTime currentDateTime = now.atStartOfDay();
        // 1. Quét danh sách mã có thể dùng (Chế độ 2 đúng Station + Chế độ 3 Public toàn sàn)
        List<Voucher> availableVouchers = voucherRepository.findAvailableVouchersForBooking(stationId, currentDateTime);

        // 2. Kiểm tra chốt chặn Chế độ 1 xem khách có đang được hưởng giảm sàn tự động không
        List<Promotion> directPromos = promotionRepository.findActiveDirectPromotionsForUser(stationId, now, customerTierId);

        // Đặt một biến cờ để kiểm tra trạng thái khóa
        boolean hasDirectPromo = (directPromos != null && !directPromos.isEmpty());

        // 3. Map dữ liệu sang DTO và gán cờ Trạng thái hiển thị cho Frontend
        List<BookingContextResponse.VoucherDTO> voucherDTOs = availableVouchers.stream()
                .map(v -> BookingContextResponse.VoucherDTO.builder()
                        .id(v.getId() != null ? v.getId().intValue() : null)
                        .voucherCode(v.getVoucherCode())
                        .discountPercentage(v.getDiscountPercentage())
                        .minOrderValue(v.getMinOrderValue())
                        //Khóa hoặc mở khóa dựa vào việc có dính giảm giá sàn tự động hay không
                        .isSelectable(!hasDirectPromo)
                        .build()
                ).toList();

        // ĐÓNG GÓI DỮ LIỆU ĐỒNG BỘ TRẢ VỀ CHO FRONTEND

        return BookingContextResponse.builder()
                .station(bookingMapper.toStationDTO(station)) // Map thẳng từ Entity Station gốc
                .bookingWindow(window)
                .vehicles(vehicleDTOS)
                .servicePackages(servicePackagePort.getAllPackages())
                .addonServices(addonServicePort.getAllAddons()
                        .stream().map(bookingMapper::toAddon).toList())
                .vouchers(voucherDTOs) // Trả ra mảng Voucher đã được gác cổng bảo mật
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
    private List<BookingContextResponse.VehicleDTO> getSubscriptionOfVehicles(List<VehicleContract> vehicleContracts,BookingContextResponse.BookingWindowDTO windowDTO){
        List<BookingContextResponse.VehicleDTO> vehicleDTOS = new ArrayList<>();
        for(VehicleContract vehicleContract : vehicleContracts){
            BookingContextResponse.VehicleDTO vehicleDTO = bookingMapper.toVehicleDTO(vehicleContract);
            BookingContextResponse.VehicleDTO.ActiveSubscription activeSubscription = getActiveSubscription(vehicleContract,windowDTO);
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
    private BookingContextResponse.VehicleDTO.ActiveSubscription getActiveSubscription(VehicleContract vehicleContract, BookingContextResponse.BookingWindowDTO windowDTO){
        Integer servicePackageId = null;
        String type = null;
        List<LocalDate> usedDates = new ArrayList<>();

        Integer familyPackageId = familySubscriptionPort.getActiveServicePackageId(vehicleContract.getId());
        Integer unlimitPackageId = unlimitSubscriptionPort.getActiveServicePackageId(vehicleContract.getId());
        if(familyPackageId!=null){
            type = "FAMILY";
            servicePackageId=familyPackageId;
            usedDates = bookingRepository.findFamilyUsedDates(
              vehicleContract.getId(),
              servicePackageId,
              windowDTO.getMinDate(),
              windowDTO.getMaxDate()
            );
        } else if (unlimitPackageId != null) {
            type = "UNLIMITTED";
            servicePackageId=unlimitPackageId;
            usedDates = bookingRepository.findUsedUnlimitBookingDates(
                    vehicleContract.getId(),
                    servicePackageId,
                    windowDTO.getMinDate(),
                    windowDTO.getMaxDate()
            );
        }

        if(unlimitPackageId!=null || familyPackageId!=null){
            return BookingContextResponse.VehicleDTO.ActiveSubscription.builder()
                    .servicePackageId(servicePackageId)
                    .type(type)
                    .usedDates(usedDates)
                    .build();
        }

        return null;

    }

}
