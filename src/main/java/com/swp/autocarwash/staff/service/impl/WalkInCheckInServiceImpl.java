package com.swp.autocarwash.staff.service.impl;

import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.booking.entity.BookingAddon;
import com.swp.autocarwash.booking.entity.BookingSlot;
import com.swp.autocarwash.booking.entity.BookingSlotAllocation;
import com.swp.autocarwash.booking.entity.enums.BookingStatus;
import com.swp.autocarwash.booking.entity.enums.BookingType;
import com.swp.autocarwash.booking.repository.BookingAddonRepository;
import com.swp.autocarwash.booking.repository.BookingRepository;
import com.swp.autocarwash.booking.repository.BookingSlotAllocationRepository;
import com.swp.autocarwash.booking.repository.BookingSlotRepository;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.entity.Vehicle;
import com.swp.autocarwash.customer.repository.CustomerRepository;
import com.swp.autocarwash.customer.repository.VehicleRepository;
import com.swp.autocarwash.payment.entity.BookingInvoice;
import com.swp.autocarwash.payment.entity.enums.BookingInvoiceStatus;
import com.swp.autocarwash.payment.repository.BookingInvoiceRepository;
import com.swp.autocarwash.queue.entity.QueueTicket;
import com.swp.autocarwash.queue.repository.custom.QueueTicketRepository;
import com.swp.autocarwash.queue.service.QueueTicketService;
import com.swp.autocarwash.servicepackage.entity.AddonService;
import com.swp.autocarwash.servicepackage.entity.PackageAddonMapping;
import com.swp.autocarwash.servicepackage.entity.ServicePackage;
import com.swp.autocarwash.servicepackage.repository.AddonServiceRepository;
import com.swp.autocarwash.servicepackage.repository.PackageAddonMappingRepository;
import com.swp.autocarwash.servicepackage.repository.ServicePackageRepository;
import com.swp.autocarwash.staff.dto.request.CalculateInvoiceRequest;
import com.swp.autocarwash.staff.dto.request.CreateWalkInRequest;
import com.swp.autocarwash.staff.dto.response.*;
import com.swp.autocarwash.staff.mapper.WalkInMapper;
import com.swp.autocarwash.staff.service.WalkInCheckInService;
import com.swp.autocarwash.station.entity.Station;
import com.swp.autocarwash.station.repository.StationRepository;
import com.swp.autocarwash.subscription.entity.FamilySubscription;
import com.swp.autocarwash.subscription.entity.UnlimitSubscription;
import com.swp.autocarwash.subscription.entity.enums.SubscriptionStatus;
import com.swp.autocarwash.subscription.repository.FamilySubscriptionRepository;
import com.swp.autocarwash.subscription.repository.UnlimitSubscriptionRepository;
import com.swp.autocarwash.system.service.impl.SystemSettingServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalkInCheckInServiceImpl implements WalkInCheckInService {

    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;
    private final WalkInMapper walkinMapper;
    private final UnlimitSubscriptionRepository unlimitSubscriptionRepository;
    private final ServicePackageRepository servicePackageRepository;
    private final AddonServiceRepository addonServiceRepository;
    private final SystemSettingServiceImpl systemSettingServiceImpl;
    private final BookingRepository bookingRepository;
    private final BookingSlotRepository bookingSlotRepository;
    private final BookingInvoiceRepository bookingInvoiceRepository;
    private final QueueTicketService queueTicketService;
    private final StationRepository stationRepository;
    private final QueueTicketRepository queueTicketRepository;
    private final BookingAddonRepository bookingAddonRepository;
    private final BookingSlotAllocationRepository bookingSlotAllocationRepository;
    private final FamilySubscriptionRepository familySubscriptionRepository;
    private final PackageAddonMappingRepository packageAddonMapping;


    //Kiểm tra sdt để phân loại đối tượng khách
    @Override
    public CheckPhoneResponse checkPhone(String phone){
        Optional<Customer> customerOpt = customerRepository.findByUserPhone(phone);
        if(customerOpt.isEmpty()){
            //khách vãng lai mới tinh, không tồn tại tài khoản trong hệ thống
            return CheckPhoneResponse.builder()
                    .existed(false).build();
        }

        //khách hàng đã có account trong hệ thống
        Customer customer = customerOpt.get();
        List<Vehicle> savedVehicles = vehicleRepository.findByCustomerIdAndIsDeletedFalse(customer.getId());
        // Chuyển thực thể sang DTO -> đưa ra list xe của account này
        List<CheckPhoneResponse.VehicleDTO> vehiclesDTO = walkinMapper.toVehicleDTOList(savedVehicles);

        LocalDate today = LocalDate.now();
        for (CheckPhoneResponse.VehicleDTO vehicleDTO : vehiclesDTO) {

            // Khởi tạo danh sách trống cho từng xe
            List<CheckPhoneResponse.VehicleSubscriptionDTO> activeSubs = new ArrayList<>();

            // Thử tìm gói Unlimited gắn với xe này
            Optional<UnlimitSubscription> unlimitOpt = unlimitSubscriptionRepository
                    .findActiveSubscriptionByVehicle(vehicleDTO.getId(), today);

            if (unlimitOpt.isPresent()) {
                UnlimitSubscription sub = unlimitOpt.get();
                activeSubs.add(CheckPhoneResponse.VehicleSubscriptionDTO.builder()
                        .subscriptionId(sub.getId())
                        .subscriptionPlanId(sub.getSubscriptionPlan().getId())
                        .servicePackageId(sub.getSubscriptionPlan().getServicePackage().getId()) // Đã bao gồm servicePackageId
                        .planName(sub.getSubscriptionPlan().getPlanName())
                        .planType(sub.getSubscriptionPlan().getPlanType())
                        .endDate(sub.getEndDate())
                        .status(sub.getStatus().name())
                        .build());
            }

            //Tiếp tục check xem xe có nằm trong nhóm Family nào có gói ACTIVE không
            Optional<FamilySubscription> familyOpt = familySubscriptionRepository
                    .findActiveFamilySubscriptionByVehicle(vehicleDTO.getId(), today);

            if (familyOpt.isPresent()) {
                FamilySubscription sub = familyOpt.get();
                activeSubs.add(CheckPhoneResponse.VehicleSubscriptionDTO.builder()
                        .subscriptionId(sub.getId())
                        .subscriptionPlanId(sub.getSubscriptionPlan().getId())
                        .servicePackageId(sub.getSubscriptionPlan().getServicePackage().getId()) // Đã bao gồm servicePackageId
                        .planName(sub.getSubscriptionPlan().getPlanName())
                        .planType(sub.getSubscriptionPlan().getPlanType())
                        .endDate(sub.getEndDate())
                        .status(sub.getStatus())
                        .build());
            }
            // Gán mảng các gói tìm được (có thể rỗng, có thể có 1 gói, hoặc cả 2 gói) vào xe
            // sở hữu gói -> hiển thị giá về 0 đồng
            vehicleDTO.setSubscriptionInfo(activeSubs);
        }

        //Trả về Response hoàn chỉnh cho Frontend
        CheckPhoneResponse response = walkinMapper.toCheckPhoneResponse(customer, vehiclesDTO);
        response.setExisted(true);
        return response;
    }

    //API tính hoá đơn tạm tính + auto load slot trống(READ)

    @Override
    public BookingSummaryResponse calculateInvoice(CalculateInvoiceRequest request){

        // Khởi tạo raw
        //nếu penalty deposit tồn tại thì transferredCredit không tồn tại và ngược lại
        BigDecimal rawAmount = BigDecimal.ZERO; // giá gói chính + addon
        BigDecimal packageDiscount = BigDecimal.ZERO;
        BigDecimal penaltyDeposit = BigDecimal.ZERO;
        BigDecimal transferredCredit = BigDecimal.ZERO;
        boolean isActionBlock = false;
        String systemNotice = "Provisional invoice valid";

        //Lấy giá tiền của gói dịch vụ chính từ DB và cộng dồn vào tổng tiền gốc
        if(request.getServicePackageId() != null){
            ServicePackage servicePackage = servicePackageRepository.findById(request.getServicePackageId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.SERVICE_PACKAGE_NOT_EXIST));
            rawAmount = rawAmount.add(servicePackage.getBasePrice());
        }

        //Lặp qua danh sách Addon dịch vụ đi kèm để cộng dồn vào tổng tiền gốc
        if (request.getAddonIds() != null && !request.getAddonIds().isEmpty()) {
            for (Integer addonId : request.getAddonIds()) {
                AddonService addon = addonServiceRepository.findById(addonId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.SERVICE_PACKAGE_ADD_ON_NOT_EXIST));

                rawAmount = rawAmount.add(addon.getPrice());
            }
        }

        // kiểm tra quyền lợi gói Membership trả trước
        Optional<Vehicle> vehicleOpt = vehicleRepository.findByLicensePlateAndIsDeletedFalse(request.getLicensePlate());
        if(request.getCustomerId() != null && vehicleOpt.isPresent() && request.getServicePackageId() != null){

            //Check xem gói mua còn hạn ACTIVE hay không
            boolean hasPackage = unlimitSubscriptionRepository.hasActiveSubscription(
                    request.getCustomerId(), vehicleOpt.get().getId(), request.getServicePackageId(), SubscriptionStatus.ACTIVE, LocalDate.now()
            );

            //Check xem trong ngày hôm nay xe này đã có lượt sử dụng gói lần nào chưa
            boolean isAllowedToday = isSubscriptionUsageAllowedToday(
                    (long) vehicleOpt.get().getId(),
                    request.getServicePackageId(),
                    LocalDate.now()
            );

            // Chỉ cho giảm trừ về 0đ nếu CÒN HẠN GÓI và CHƯA DÙNG LẦN NÀO TRONG NGÀY
            if(hasPackage && isAllowedToday){
                ServicePackage mainPackage = servicePackageRepository.findById(request.getServicePackageId()).get();
                packageDiscount = mainPackage.getBasePrice();
            } else if (hasPackage && !isAllowedToday) {
                //cảnh báo ra UI để Staff giải thích cho khách
                systemNotice += " | Notice: This subscription package has already been used once today. Standard pricing applies.";
            }
        }

        //Kiểm tra lịch sử phạt vi phạm của biến số xe vãng lai
        if(vehicleOpt.isPresent()){
            Vehicle vehicle = vehicleOpt.get();
            if(request.getCustomerId() == null && vehicle.getViolationCount() != null && vehicle.getViolationCount() > 3
                    && vehicle.getRestrictedUntil()!=null&& vehicle.getRestrictedUntil().isAfter(Instant.now())){
                //systemSettingServiceImpl.getMaxViolationLimit("MAX_VIOLATION_LIMIT")
                penaltyDeposit = systemSettingServiceImpl.getDepositAmount(SystemSettingServiceImpl.DEFAULT_DEPOSIT_AMOUNT);
                //kích hoạt mức nộp tiền cọc 20k trước vào sử dụng dịch vụ
                isActionBlock = true; //khoá nút CONFIRM CHECK IN, đòi đóng cọc trước
                systemNotice = "The system require staff get deposit before confirm check-in";

            }
        }

        //Chế độ xem số tiền "Cứu cọc" từ đơn trễ hẹn trong ngày
        //với cái xe này thì trong ngày nay có booking nào để chuyển cọc
        Optional<Booking> oldBookingOpt = bookingRepository.findBookingToRescueDeposit(request.getLicensePlate(),LocalDate.now(),
                BookingStatus.NO_SHOW.toString());
        if(oldBookingOpt.isPresent()){
            Booking oldBooking = oldBookingOpt.get();
            transferredCredit = systemSettingServiceImpl.getDepositAmount(SystemSettingServiceImpl.DEFAULT_DEPOSIT_AMOUNT);
            systemNotice += " | The system found late appointment today, support auto transfer deposit to new booking";
        }

        //Công thức tính số tiền thực tế cần thu tại quầy lúc lấy xe
        BigDecimal remainingBalance = rawAmount.subtract(packageDiscount).subtract(penaltyDeposit).subtract(transferredCredit);
        if (remainingBalance.compareTo(BigDecimal.ZERO) < 0) {
            remainingBalance = BigDecimal.ZERO;
        }

        //Hiển thị slot cho đặt, gom slot trả về slot tương ứng tổng thời gian cần thiết
        if (request.getStationId() == null) {
            throw new BusinessException(ErrorCode.STATION_NOT_FOUND);
        }

        //Tính tổng số slot bắt buộc cần có (Gói chính + Các Addon)
        ServicePackage servicePackage = servicePackageRepository.findById(request.getServicePackageId())
                .orElseThrow(() -> new BusinessException(ErrorCode.SERVICE_PACKAGE_NOT_EXIST));
        int totalRequiredSlots = servicePackage.getRequiredSlot();

        if (request.getAddonIds() != null && !request.getAddonIds().isEmpty()) {
            for (Integer addonId : request.getAddonIds()) {
                AddonService addon = addonServiceRepository.findById(addonId).get();
                if (addon.getDurationMinutes() != null && addon.getDurationMinutes() > 0) {
                    int addonSlots = (int) Math.ceil((double) addon.getDurationMinutes() / 15);
                    totalRequiredSlots += addonSlots;
                }
            }
        }

        //Lấy toàn bộ các slot còn trống trong ngày hôm nay từ thời điểm hiện tại trở đi
        LocalTime currentSystemTime = LocalTime.now();
        List<BookingSlot> dbSlots = bookingSlotRepository.findAvailableSlotsByStationAndDate(request.getStationId(), LocalDate.now(), currentSystemTime);

        // Sắp xếp danh sách slot theo thứ tự thời gian tăng dần để quét liên tiếp
        List<BookingSlot> sortedDbSlots = dbSlots.stream()
                .sorted(Comparator.comparing(BookingSlot::getStartTime))
                .toList();

        List<BookingSummaryResponse.AvailableSlotDTO> validStartSlots = new ArrayList<>();

        //Thuật toán quét chuỗi liên tiếp
        for (int i = 0; i <= sortedDbSlots.size() - totalRequiredSlots; i++) {
            boolean isBlockValid = true;

            List<Long> clusterSlotIds = new ArrayList<>();

            // Kiểm tra xem từ vị trí i có đủ 'totalRequiredSlots' liên tiếp không
            for (int j = 0; j < totalRequiredSlots; j++) {
                BookingSlot current = sortedDbSlots.get(i + j);

                //Slot đó không được FULL công suất
                if ("FULL".equals(current.getStatus()) || current.getBookedCount() >= current.getMaxCapacity()) {
                    isBlockValid = false;
                    break;
                }

                //Tính liên tiếp về mặt thời gian (chỉ check từ phần tử thứ 2 của cụm)
                if (j > 0) {
                    BookingSlot previous = sortedDbSlots.get(i + j - 1);
                    if (!previous.getEndTime().equals(current.getStartTime())) {
                        isBlockValid = false;
                        break;
                    }
                }
                clusterSlotIds.add(current.getId());
            }

            // Nếu tìm được một cụm liên tiếp đủ chỗ, lấy thằng đầu tiên làm đại diện đại diện giờ bắt đầu trên UI
            if (isBlockValid) {
                BookingSlot startSlot = sortedDbSlots.get(i);

                BookingSummaryResponse.AvailableSlotDTO dto = BookingSummaryResponse.AvailableSlotDTO.builder()
                        .slotId (startSlot.getId()) // Gửi ID của slot bắt đầu
                        .startTime(startSlot.getStartTime())
                        .endTime(sortedDbSlots.get(i + totalRequiredSlots - 1).getEndTime()) // End time là của slot cuối cụm
                        .associatedSlotIds(clusterSlotIds) //Trả về cả mảng ID để FE bấm 1 phát gửi lên hết cả cụm này
                        .build();

                validStartSlots.add(dto);
            }
        }

        return BookingSummaryResponse.builder()
                .rawAmount(rawAmount)
                .packageDiscount(packageDiscount)
                .penaltyDeposit(penaltyDeposit)
                .transferredCredit(transferredCredit)
                .remainingBalance(remainingBalance)
                .systemNotice(systemNotice)
                .isActionBlock(isActionBlock)
                .availableSlots(validStartSlots)
                .build();
    }

    //BẤM NÚT XÁC NHẬN TẠO ĐƠN THẬT
    //tránh mất tiền cọc: kiểu chọn nhưng chưa xác nhận thì k có chuyển cọc đi lung tung -> mất cọc
    @Override
    public CreateWalkInResponse createWalkInOrder(CreateWalkInRequest request) {

        //Cứu cọc, tìm đơn trễ hẹn trong ngày
        Optional<Booking> oldBookingOpt = bookingRepository.findBookingToRescueDeposit(request.getLicensePlate(), LocalDate.now(), BookingStatus.NO_SHOW.toString());
        BigDecimal creditFromOldBooking = BigDecimal.ZERO;

        if (oldBookingOpt.isPresent()) {
            Booking oldBooking = oldBookingOpt.get();
            // Lấy số tiền cọc cấu hình từ SystemSetting thay vì số cứng hoặc lấy trong Booking
            creditFromOldBooking = systemSettingServiceImpl.getDepositAmount(SystemSettingServiceImpl.DEFAULT_DEPOSIT_AMOUNT);

            // Đổi trạng thái sang CANCELED để bảo vệ tiền cọc không bị con bot Cron-job quét nuốt cọc cuối ngày
            oldBooking.setStatus(BookingStatus.CANCELED.name()  );
            bookingRepository.save(oldBooking);
        }

        //khởi tạo và cộng dồn tổng tiền dịch vụ gốc
        BigDecimal rawAmount = BigDecimal.ZERO;
        // Cộng dồn tiền Gói dịch vụ chính
        ServicePackage servicePackage = null;
        if (request.getServicePackageId() != null) {
            servicePackage = servicePackageRepository.findById(request.getServicePackageId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.SERVICE_PACKAGE_NOT_EXIST));
            rawAmount = rawAmount.add(servicePackage.getBasePrice());
        }

        //Đăng ký hoặc cập nhật thông tin phương tiện (Lấy thực thể xe lên trước để validate)
        Optional<Vehicle> vehicleOpt = vehicleRepository.findByLicensePlateAndIsDeletedFalse(request.getLicensePlate());

        // VALIDATE XE CÓ THUỘC CUSTOMER ĐÓ HAY KHÔNG
        // Nếu là khách vãng lai hoàn toàn thì ko check
        if (request.getCustomerId() != null && vehicleOpt.isPresent()) {
            Vehicle vehicle = vehicleOpt.get();
            if (vehicle.getCustomer() != null && !vehicle.getCustomer().getId().equals(request.getCustomerId())) {
                throw new BusinessException(ErrorCode.VEHICLE_NOT_BELONG_TO_CUSTOMER);
            }
        }

        //MỘT XE KHÔNG THỂ ĐẶT TRÙNG KHUNG GIỜ NHIỀU ĐƠN TRONG NGÀY
        if (request.getChosenSlotIds() != null && !request.getChosenSlotIds().isEmpty() && vehicleOpt.isPresent()) {
            boolean isDuplicateBooking = bookingRepository.existsByVehicleIdAndDateAndSlotIds(
                    vehicleOpt.get().getId(),
                    LocalDate.now(),
                    request.getChosenSlotIds()
            );
            if (isDuplicateBooking) {
                throw new BusinessException(ErrorCode.VEHICLE_ALREADY_BOOKED_THIS_SLOT);
            }
        }

        // Lấy danh sách các Slot thực tế từ Database để kiểm tra tính liên tiếp và thời gian
        List<BookingSlot> selectedSlots = bookingSlotRepository.findAllById(request.getChosenSlotIds());
        if (selectedSlots.size() != request.getChosenSlotIds().size()) {
            throw new BusinessException(ErrorCode.SERVICE_SLOT_NOT_AVAILABLE);
        }

        // LOGIC: TÍNH TỔNG SỐ SLOT YÊU CẦU BAO GỒM CẢ ADD-ON
        int totalRequiredSlotCount = 0;
        if (servicePackage != null) {
            totalRequiredSlotCount += servicePackage.getRequiredSlot();
        }
        if (request.getAddonIds() != null && !request.getAddonIds().isEmpty()) {
            for (Integer addonId : request.getAddonIds()) {
                AddonService addon = addonServiceRepository.findById(addonId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.SERVICE_PACKAGE_ADD_ON_NOT_EXIST));

                if (addon.getDurationMinutes() != null && addon.getDurationMinutes() > 0) {
                    int addonSlots = (int) Math.ceil((double) addon.getDurationMinutes() / 15);
                    totalRequiredSlotCount += addonSlots;
                }
            }
        }

        // Kiểm tra xem số lượng slot Frontend gửi lên có khớp với tổng yêu cầu thực tế không
        if (selectedSlots.size() != totalRequiredSlotCount) {
            throw new BusinessException(ErrorCode.INVALID_SLOT_QUANTITY);
        }

        // Sắp xếp các slot đã chọn theo thời gian bắt đầu tăng dần để kiểm tra tính liên tiếp
        List<BookingSlot> sortedSlots = selectedSlots.stream()
                .sorted(Comparator.comparing(BookingSlot::getStartTime))
                .toList();

        for (int i = 0; i < sortedSlots.size() - 1; i++) {
            LocalTime currentEnd = sortedSlots.get(i).getEndTime();
            LocalTime nextStart = sortedSlots.get(i + 1).getStartTime();
            if (!currentEnd.equals(nextStart)) {
                throw new BusinessException(ErrorCode.SLOTS_MUST_BE_CONSECUTIVE);
            }
        }

        // Cộng dồn tiền các Addon đính kèm lẻ (nếu có)
        if (request.getAddonIds() != null && !request.getAddonIds().isEmpty()) {
            for (Integer addonId : request.getAddonIds()) {
                AddonService addon = addonServiceRepository.findById(addonId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.SERVICE_PACKAGE_ADD_ON_NOT_EXIST));
                rawAmount = rawAmount.add(addon.getPrice());
            }
        }

        // Kiểm tra quyền lợi gói membership unlimited (nếu là member)
        BigDecimal packageDiscount = BigDecimal.ZERO;

        if (request.getCustomerId() != null && vehicleOpt.isPresent() && request.getServicePackageId() != null) {

            boolean hasPackage = unlimitSubscriptionRepository.hasActiveSubscription(
                    request.getCustomerId(),
                    vehicleOpt.get().getId(),
                    request.getServicePackageId(),
                    SubscriptionStatus.ACTIVE,
                    LocalDate.now()
            );

            //Check giới hạn 1 ngày/ 1 lần
            boolean isAllowedToday = isSubscriptionUsageAllowedToday(
                    (long) vehicleOpt.get().getId(),
                    request.getServicePackageId(),
                    LocalDate.now()
            );

            if (hasPackage && isAllowedToday) {
                // Khớp gói đã mua và chưa dùng lần nào hôm nay -> Miễn phí tiền Gói dịch vụ chính
                ServicePackage mainPackage = servicePackageRepository.findById(request.getServicePackageId()).get();
                packageDiscount = mainPackage.getBasePrice();
            }
        }

        //kiểm tra và check bảo mật cọc phạt(chỉ áp dụng khách vãng lai)
        //check nếu chưa đóng tiền thì không cho confirm check in
        BigDecimal penaltyDeposit = BigDecimal.ZERO;
        if (request.getCustomerId() == null && vehicleOpt.isPresent()) {
            Vehicle vehicle = vehicleOpt.get();

            // Kiểm tra xem xe có đang dính án phạt đóng băng hay không
            if (vehicle.getViolationCount() != null && vehicle.getViolationCount() > 3
                    && vehicle.getRestrictedUntil() != null && vehicle.getRestrictedUntil().isAfter(Instant.now())) {

                // Lấy số tiền phạt cấu hình từ hệ thống
                penaltyDeposit = systemSettingServiceImpl.getDepositAmount(SystemSettingServiceImpl.DEFAULT_DEPOSIT_AMOUNT);

                //FE gửi lên báo chưa thu tiền
                if (request.getPenaltyDepositCollected() == null || !request.getPenaltyDepositCollected()) {
                    // Chặn đứng diện tạo đơn và bắn lỗi để FE mở Popup thu tiền tại quầy
                    throw new BusinessException(ErrorCode.PENALTY_DEPOSIT_NOT_CONFIRMED);
                }

            }
        }

            //đăng ký hoặc cập nhật thông tin phương tiện
            Vehicle finalVehicle = vehicleOpt.orElseGet(() -> {
                // Xe mới tinh diện vãng lai -> Tạo mới hoàn toàn dựa vào các field chi tiết từ Request
                Vehicle newVehicle = Vehicle.builder()
                        .licensePlate(request.getLicensePlate())
                        .brandName(request.getBrandName() != null ? request.getBrandName() : "Customer Walk-in")
                        .color(request.getColor())
                        .violationCount(0)
                        .isDeleted(false)
                        .build();
                return vehicleRepository.save(newVehicle);
            });

        //Giảm giá THẬT sự từ hệ thống (Chỉ gồm giảm giá gói cước Membership/Unlimited)
        BigDecimal realPackageDiscount = packageDiscount;

        //Tổng giá trị đơn hàng sau khi ĐÃ TRỪ GIẢM GIÁ, không trừ cọc
        BigDecimal totalAmountAfterDiscount = rawAmount.subtract(realPackageDiscount);
        if (totalAmountAfterDiscount.compareTo(BigDecimal.ZERO) < 0) {
            totalAmountAfterDiscount = BigDecimal.ZERO;
        }

        //Số tiền thực tế còn lại cần thu lúc Checkout
        BigDecimal remainingBalanceAtCheckout = totalAmountAfterDiscount
                .subtract(penaltyDeposit)
                .subtract(creditFromOldBooking);
        if (remainingBalanceAtCheckout.compareTo(BigDecimal.ZERO) < 0) {
            remainingBalanceAtCheckout = BigDecimal.ZERO;
        }

            //LẬP LỊCH HẸN MỚI (BOOKING) VỚI TRẠNG THÁI CHECKED_IN MẶC ĐỊNH
            Booking newBooking = Booking.builder()
                    .vehicle(finalVehicle)
                    .customer(request.getCustomerId() != null ? customerRepository.findById(request.getCustomerId()).orElse(null) : null)
                    .servicePackage(servicePackage)
                    .appointmentDate(LocalDate.now())
                    .status(BookingStatus.CHECK_IN.name()) // Mặc định chuyển thẳng sang CHECKED_IN theo luồng Walk-In tại quầy
                    .bookingType(BookingType.WALK_IN.name())
                    .createdAt(LocalDateTime.now())
                    .checkInAt(LocalDateTime.now())
                    .isDepositPaid(creditFromOldBooking.compareTo(BigDecimal.ZERO) > 0
                || (request.getPenaltyDepositCollected() != null && request.getPenaltyDepositCollected()))
                    .build();
            Booking savedBooking = bookingRepository.save(newBooking);

            //TRỪ CÔNG SUẤT SLOT VÀ LƯU BẢNG BOOKING_SLOT_ALLOCATION
            for (BookingSlot slot : selectedSlots) {
                if ("FULL".equals(slot.getStatus()) || slot.getBookedCount() >= slot.getMaxCapacity()) {
                    throw new BusinessException(ErrorCode.SERVICE_SLOT_NOT_AVAILABLE);
                }
                slot.setBookedCount(slot.getBookedCount() + 1);
                if (slot.getBookedCount() >= slot.getMaxCapacity()) {
                    slot.setStatus("FULL");
                }
                bookingSlotRepository.save(slot);

                // Thực hiện ghi nhận phân bổ ô giờ đặt lịch cho Đơn hàng này
                BookingSlotAllocation allocation = BookingSlotAllocation.builder()
                        .booking(savedBooking)
                        .bookingSlot(slot)
                        .build();
                bookingSlotAllocationRepository.save(allocation);
            }

            //LOGIC: LƯU DATA CHI TIẾT XUỐNG BẢNG BOOKING_ADDON (NẾU CÓ)
            if (request.getAddonIds() != null && !request.getAddonIds().isEmpty()) {
                for (Integer addonId : request.getAddonIds()) {
                    AddonService addon = addonServiceRepository.findById(addonId).get();
                    BookingAddon bookingAddon = BookingAddon.builder()
                            .booking(savedBooking)
                            .addonService(addon)
                            .price(addon.getPrice()) // Lưu snapshot giá
                            .build();
                    bookingAddonRepository.save(bookingAddon);
                }
            }

            //Tách biệt doanh thu dịch vụ chính và dịch vụ bổ sung addon để lưu thống kê
            BigDecimal serviceAmount = BigDecimal.ZERO;
            BigDecimal addonAmount = BigDecimal.ZERO;
            //ServicePackage servicePackage = null;
            if (request.getServicePackageId() != null) {
                servicePackage = servicePackageRepository.findById(request.getServicePackageId()).get();
                serviceAmount = servicePackage.getBasePrice();
            }

            //TÍNH TOÁN CHI TIẾT DÒNG TIỀN VÀ PHÁT HÀNH HÓA ĐƠN TỔNG BookingInvoice
            if (request.getAddonIds() != null && !request.getAddonIds().isEmpty()) {
                for (Integer addonId : request.getAddonIds()) {
                    AddonService addon = addonServiceRepository.findById(addonId).get();
                    addonAmount = addonAmount.add(addon.getPrice());
                }
            }

            // Ghi lại đúng số tiền lên chính Booking (không chỉ BookingInvoice) — nếu không,
            // các field này giữ nguyên default 0  khiến GET booking detail /
            // Payment page luôn hiện Total Due = 0 cho mọi đơn walk-in.
            savedBooking.setTotalServiceAmount(serviceAmount);
            savedBooking.setTotalAddonAmount(addonAmount);
            savedBooking.setVoucherDiscountAmount(realPackageDiscount);
            savedBooking.setTotalAmount(totalAmountAfterDiscount);
            bookingRepository.save(savedBooking);

            //KHỞI TẠO ĐẦY ĐỦ THỰC THỂ BOOKING_INVOICE
            //LƯU DATA XUỐNG BẢNG BOOKING_INVOICE KHI HOÀN TẤT CHECK-IN
            BookingInvoice invoice = BookingInvoice.builder()
                    .booking(savedBooking)
                    .customer(savedBooking.getCustomer()) //có thể null nếu khách vãng lai mới
                    //Các trường dòng tiền tổng
                    .rawAmount(rawAmount) // Tổng tiền gốc ban đầu (serviceAmount + addonAmount)
                    .discountAmount(realPackageDiscount) // Chỉ lưu giảm giá gói thật
                    .finalAmount(totalAmountAfterDiscount) // Giá sau giảm giá (chưa trừ cọc)
                    //Các trường bóc tách doanh thu dịch vụ cụ thể
                    .serviceAmount(serviceAmount) // Tổng giá trị của riêng dịch vụ chính
                    .addonAmount(addonAmount) // Tổng giá trị của các dịch vụ bổ sung đính kèm
                    //Các trường giảm giá từ Loyalty/Khuyến mãi (Luồng Walk-in tại quầy mặc định khởi tạo = 0.00)
                    .voucherDiscount(BigDecimal.ZERO)
                    .pointDiscount(BigDecimal.ZERO)
                    //Trạng thái và Thời gian
                    .status(BookingInvoiceStatus.PENDING.name()) // Trạng thái hóa đơn ban đầu: Chờ rửa xe xong khách ra trả tiền mới đổi sang PAID
                    .paidAt(null) // Chưa thanh toán thành công lúc check-in nên để null
                    .build();

            bookingInvoiceRepository.save(invoice);

            //CẤP PHÁT SỐ VÉ THỨ TỰ HÀNG ĐỢI VẬT LÝ (QueueTicket)
            //Sinh số thứ tự vé tiếp theo dựa vào stationId và diện khách Walk-In (false)
            String nextTicketNumber = queueTicketService.generateTicketNumber(request.getStationId(), false);

            //gọi từ StationRepository để làm khóa ngoại station_id.

            Station currentStation = stationRepository.findById(request.getStationId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.STATION_NOT_FOUND));

            //KHỞI TẠO ĐẦY ĐỦ THỰC THỂ QUEUE_TICKET THEO DB
            QueueTicket ticket = QueueTicket.builder()
                    .station(currentStation) //Vé thuộc chi nhánh nào
                    .booking(savedBooking)   //trỏ sang lịch hẹn vừa tạo booking_id
                    .ticketNumber(nextTicketNumber) // Số thứ tự hiển thị ticket_number
                    //Trạng thái và Phân loại
                    .status(BookingStatus.CHECK_IN.name())    // Trạng thái vé status
                    .isBooking(false)        // Đánh dấu KHÔNG PHẢI đơn đặt trước
                    .priorityScore(queueTicketService.computePriorityScore(savedBooking.getCustomer(), false)) // Tier weight (mặc định MEMBER nếu khách vãng lai chưa có tài khoản)
                    .build();


            QueueTicket savedTicket = queueTicketRepository.save(ticket);


            return CreateWalkInResponse.builder()
                    .bookingId(savedBooking.getId())
                    .queueTicketId(ticket.getId())
                    .ticketNumber(nextTicketNumber)
                    .status(savedBooking.getStatus())
                    .remainingBalance(remainingBalanceAtCheckout)
                    .checkInAt(savedBooking.getCheckInAt())
                    .message("Walk-in booking has been created successfully. The vehicle has been added to the service queue.")
                    .build();
        }

    // Lấy toàn bộ danh sách gói dịch vụ và addon phục vụ việc dựng Form chọn tại quầy
    @Override
    public WalkInFormDataResponse getWalkInFormData() {

        // Lấy tất cả gói dịch vụ chính từ Database lên
        List<ServicePackage> packages = servicePackageRepository.findByIsDeletedFalse();

        // Khởi tạo một danh sách rỗng để chứa các DTO sau khi chuyển đổi
        List<WalkInFormDataResponse.ServicePackageDTO> packageDTOs = new ArrayList<>();

        // Duyệt qua từng thực thể gói dịch vụ bằng vòng lặp for-each
        for (ServicePackage p : packages) {
            WalkInFormDataResponse.ServicePackageDTO dto = WalkInFormDataResponse.ServicePackageDTO.builder()
                    .id(p.getId())
                    .name(p.getName())
                    .basePrice(p.getBasePrice())
                    .requiredSlot(p.getRequiredSlot())
                    .description(p.getDescription())
                    .build();

            packageDTOs.add(dto);
        }

        // Lấy tất cả dịch vụ bổ sung từ Database lên
        List<AddonService> addons = addonServiceRepository.findByIsDeletedFalse();
        List<PackageAddonMapping> allMapping = packageAddonMapping.findAllMappings();
        List<WalkInFormDataResponse.AddonServiceDTO> addonDTOs = new ArrayList<>();
        for(AddonService a  : addons){
            List<Integer> packageIds = new ArrayList<>();
            for(PackageAddonMapping p : allMapping){
                if(a.getId().equals(p.getId().getAddonServiceId())){
                    packageIds.add(p.getId().getServicePackageId());
                }
            }

            WalkInFormDataResponse.AddonServiceDTO dto = WalkInFormDataResponse.AddonServiceDTO.builder()
                    .id(a.getId())
                    .name(a.getName())
                    .price(a.getPrice())
                    .description(a.getDescription())
                    .durationMinutes(a.getDurationMinutes())
                    .includedInPackageIds(packageIds)
                    .build();
            addonDTOs.add(dto);
        }

        return WalkInFormDataResponse.builder()
                .servicePackages(packageDTOs)
                .addonServices(addonDTOs)
                .build();
    }

    /**
     * API XÁC NHẬN THU TIỀN CỌC PHẠT 20K TẠI QUẦY (TRƯỚC KHI TẠO ĐƠN)
     * Luồng đi: Staff thấy thông báo phạt -> Thu 20k tiền mặt của khách -> Bấm nút [XÁC NHẬN ĐÃ THU]
     */
    @Override
    @Transactional
    public CheckInResultResponse collectWalkInPenaltyDeposit(String licensePlate) {

        // 1. Tìm xem chiếc xe vãng lai này có tồn tại trong hệ thống không
        Vehicle vehicle = vehicleRepository.findByLicensePlateAndIsDeletedFalse(licensePlate)
                .orElseThrow(() -> new BusinessException(ErrorCode.VEHICLE_NOT_FOUND));

        // 2. Kiểm tra xem xe này có thực sự đang trong diện bị phạt/hạn chế không (vi phạm > 3 lần và chưa hết hạn phạt)
        if (vehicle.getViolationCount() == null || vehicle.getViolationCount() <= systemSettingServiceImpl.getMaxViolationLimit("MAX_VIOLATION_LIMIT")
                || vehicle.getRestrictedUntil() == null || vehicle.getRestrictedUntil().isBefore(Instant.now())) {
            throw new BusinessException(ErrorCode.VEHICLE_CLEAR_NO_PENALTY);
        }

        // 3. Trả về phản hồi cho Frontend biết là BE đã xác thực xe dính án phạt và hợp lệ để thu cọc
        // Sau khi nhận được cái này, FE sẽ set biến State thành true để mở khóa nút [TẠO ĐƠN & CHECK-IN]
        return CheckInResultResponse.builder()
                .message("Collected 20,000 VND penalty deposit successfully for vehicle " + licensePlate + ". You may now proceed to create the walk-in order.")
                .requiresWalkIn(false)
                .build();
    }

    private boolean  isSubscriptionUsageAllowedToday(Long vehicleId, Integer servicePackageId, LocalDate date) {
        // Check xem trong ngày hôm đó xe này đã có booking nào sử dụng gói này mà KHÔNG PHẢI bị hủy (CANCELED) hay chưa
        boolean hasUsedToday = bookingRepository.existsByVehicleIdAndServicePackageIdAndAppointmentDateAndStatusNot(
                vehicleId,
                servicePackageId,
                date,
                BookingStatus.CANCELED.name()
        );
        // Nếu chưa dùng hôm nay -> Trả về true (Cho phép áp dụng giá 0đ), ngược lại trả về false (Ép về giá gốc)
        return !hasUsedToday;
    }

}
