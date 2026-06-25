package com.swp.autocarwash.staff.service.impl;

import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.booking.entity.BookingSlot;
import com.swp.autocarwash.booking.entity.enums.BookingStatus;
import com.swp.autocarwash.booking.repository.BookingRepository;
import com.swp.autocarwash.booking.repository.BookingSlotRepository;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.entity.Vehicle;
import com.swp.autocarwash.customer.repository.CustomerRepository;
import com.swp.autocarwash.customer.repository.VehicleRepository;
import com.swp.autocarwash.payment.entity.BookingInvoice;
import com.swp.autocarwash.payment.repository.custom.BookingInvoiceRepository;
import com.swp.autocarwash.queue.entity.QueueTicket;
import com.swp.autocarwash.queue.repository.custom.QueueTicketRepository;
import com.swp.autocarwash.queue.service.QueueTicketService;
import com.swp.autocarwash.servicepackage.entity.AddonService;
import com.swp.autocarwash.servicepackage.entity.ServicePackage;
import com.swp.autocarwash.servicepackage.repository.AddonServiceRepository;
import com.swp.autocarwash.servicepackage.repository.ServicePackageRepository;
import com.swp.autocarwash.staff.dto.request.CalculateInvoiceRequest;
import com.swp.autocarwash.staff.dto.request.CreateWalkInRequest;
import com.swp.autocarwash.staff.dto.response.BookingSummaryResponse;
import com.swp.autocarwash.staff.dto.response.CheckPhoneResponse;
import com.swp.autocarwash.staff.dto.response.CreateWalkInResponse;
import com.swp.autocarwash.staff.mapper.WalkInMapper;
import com.swp.autocarwash.station.entity.Station;
import com.swp.autocarwash.station.repository.StationRepository;
import com.swp.autocarwash.subscription.entity.enums.SubscriptionStatus;
import com.swp.autocarwash.subscription.repository.custom.UnlimitSubscriptionRepository;
import com.swp.autocarwash.system.service.impl.SystemSettingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalkInCheckInService {

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


    //Kiểm tra sdt để phân loại đối tượng khách cũ/mới(SELECT)
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
        //Đổi thực thể sang DTO -> trả cho FE
        List<CheckPhoneResponse.VehicleDTO> vehiclesDTO = walkinMapper.toVehicleDTOList(savedVehicles);
        return walkinMapper.toCheckPhoneResponse(customer,vehiclesDTO);
    }

    //API tính hoá đơn tạm tính + auto load slot trống(READ)

    public BookingSummaryResponse calculateInvoice(CalculateInvoiceRequest request){

        // Khởi tạo raw
        //nếu penalty deposit tồn tại thì transferredCredit không tồn tại và ngược lại
        BigDecimal rawAmount = BigDecimal.ZERO;
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

        // 2. Lặp qua danh sách Addon dịch vụ đi kèm để cộng dồn vào tổng tiền gốc
        if (request.getAddonIds() != null && !request.getAddonIds().isEmpty()) {
            for (Integer addonId : request.getAddonIds()) {
                AddonService addon = addonServiceRepository.findById(addonId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.SERVICE_PACKAGE_ADD_ON_NOT_EXIST));

                rawAmount = rawAmount.add(addon.getPrice());
            }
        }

        //kiểm tra quyền lợi gói Membership trả trước
        // nếu walkin này là customer có account tron system, coi thử customer này đang sở hữu gói membership unli/fml hay không
        //để biết mà cho họ áp dụng sử dụng tại walkin luôn
        Optional<Vehicle> vehicleOpt = vehicleRepository.findByLicensePlateAndIsDeletedFalse(request.getLicensePlate());
        if(request.getCustomerId() != null && vehicleOpt.isPresent()&& request.getServicePackageId() != null){
            boolean hasPackage = unlimitSubscriptionRepository.hasActiveSubscription(
                    request.getCustomerId(), vehicleOpt.get().getId(),request.getServicePackageId() , SubscriptionStatus.ACTIVE.toString(),LocalDate.now()
            );
            if(hasPackage){
                //Nếu khớp gói đã mua, lấy đúng giá của gói dịch vụ trừ đi tiền được giảm
                ServicePackage mainPackage = servicePackageRepository.findById(request.getServicePackageId()).get();
                packageDiscount = mainPackage.getBasePrice();
                //khách được free tiền gói chính nhưng vẫn phải tính tiền gói add on lẻ
            }
        }

        //Kiểm tra lịch sử phạt vi phạm của biến số xe vãng lai
        if(vehicleOpt.isPresent()){
            Vehicle vehicle = vehicleOpt.get();
            if(request.getCustomerId() == null && vehicle.getViolationCount() != null && vehicle.getViolationCount() > 3
                    && vehicle.getRestrictedUntil()!=null&& vehicle.getRestrictedUntil().isAfter(Instant.now())){
                penaltyDeposit = systemSettingServiceImpl.getDepositAmount(SystemSettingServiceImpl.DEFAULT_DEPOSIT_AMOUNT);
                //kích hoạt mức nộp tiền cọc 20k trước vào sử dụng dịch vụ
                isActionBlock = true; //báo hiệu khoá nút CONFIRM CHECK IN, đòi đóng cọc trước
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
            systemNotice +="The system found late appointment today, support auto transfer deposit to new booking";
        }

        //Công thức tính số tiền thực tế cần thu tại quầy lúc lấy xe
        BigDecimal remainingBalance = rawAmount.subtract(packageDiscount).subtract(penaltyDeposit);
        if (remainingBalance.compareTo(BigDecimal.ZERO) < 0) {
            remainingBalance = BigDecimal.ZERO;
        }

        //AUTO LOAD REAL-TIME: Tự động quét và đóng gói các Slot thực sự còn trống để trả về cho Front-end hiển thị
        List<BookingSlot> dbSlots = bookingSlotRepository.findAvailableSlotsByStationAndDate(request.getStationId(), LocalDate.now());

        List<BookingSummaryResponse.AvailableSlotDTO> availableSlots = walkinMapper.toAvailableSlotDTOList(dbSlots);

        return BookingSummaryResponse.builder()
                .rawAmount(rawAmount)
                .packageDiscount(packageDiscount)
                .penaltyDeposit(penaltyDeposit)
                .transferredCredit(transferredCredit)
                .remainingBalance(remainingBalance)
                .systemNotice(systemNotice)
                .isActionBlock(isActionBlock)
                .availableSlots(availableSlots) // Front-end nhận mảng này để auto hiển thị ô giờ sáng/mờ có thể booking
                .build();
    }

    //BẤM NÚT XÁC NHẬN TẠO ĐƠN THẬT(GHI DỮ LIỆU ĐỒNG THỜI TRANSACTION)
    //tránh mất tiền cọc: kiểu chọn nhưng chưa xác nhận thì k có chuyển cọc đi lung tung -> mất cọc

    public CreateWalkInResponse createWalkInOrder(CreateWalkInRequest request) {

        //Cứu cọc, tìm đơn trễ hẹn trong ngày
        Optional<Booking> oldBookingOpt = bookingRepository.findBookingToRescueDeposit(request.getLicensePlate(), LocalDate.now(), BookingStatus.NO_SHOW.toString());
        BigDecimal creditFromOldBooking = BigDecimal.ZERO;

        if (oldBookingOpt.isPresent()) {
            Booking oldBooking = oldBookingOpt.get();
            // Lấy số tiền cọc cấu hình từ SystemSetting thay vì số cứng hoặc lấy trong Booking
            creditFromOldBooking = systemSettingServiceImpl.getDepositAmount(SystemSettingServiceImpl.DEFAULT_DEPOSIT_AMOUNT);

            // Đổi trạng thái sang CANCELED để bảo vệ tiền cọc không bị con bot Cron-job quét nuốt cọc cuối ngày
            oldBooking.setStatus("CANCELED");
            bookingRepository.save(oldBooking);
        }

        //khởi tạo và cộng dồn tổng tiền dịch vụ gốc
        BigDecimal rawAmount = BigDecimal.ZERO;
        // Cộng dồn tiền Gói dịch vụ chính
        if (request.getServicePackageId() != null) {
            ServicePackage servicePackage = servicePackageRepository.findById(request.getServicePackageId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.SERVICE_PACKAGE_NOT_EXIST));
            rawAmount = rawAmount.add(servicePackage.getBasePrice());
        }

        // Cộng dồn tiền các Addon đính kèm lẻ (nếu có)
        if (request.getAddonIds() != null && !request.getAddonIds().isEmpty()) {
            for (Integer addonId : request.getAddonIds()) {
                AddonService addon = addonServiceRepository.findById(addonId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.SERVICE_PACKAGE_ADD_ON_NOT_EXIST));
                rawAmount = rawAmount.add(addon.getPrice());
            }
        }

        //Kiểm tra quyền lợi gói membership unlimited (nếu là member)
        BigDecimal packageDiscount = BigDecimal.ZERO;
        Optional<Vehicle> vehicleOpt = vehicleRepository.findByLicensePlateAndIsDeletedFalse(request.getLicensePlate());

        if (request.getCustomerId() != null && vehicleOpt.isPresent() && request.getServicePackageId() != null) {

            boolean hasPackage = unlimitSubscriptionRepository.hasActiveSubscription(
                    request.getCustomerId(),
                    vehicleOpt.get().getId(),
                    request.getServicePackageId(),
                    SubscriptionStatus.ACTIVE.toString(),
                    LocalDate.now()
            );

            if (hasPackage) {
                // Khớp gói đã mua -> Miễn phí phần tiền của Gói dịch vụ chính (Vẫn tính tiền Addon nếu có)
                ServicePackage mainPackage = servicePackageRepository.findById(request.getServicePackageId()).get();
                packageDiscount = mainPackage.getBasePrice();
            }
        }

        //kiểm tra và check bảo mật cọc phạt(chỉ áp dụng khách vãng lai)
        //check nếu chưa đóng tiền thì không cho confirm check in
        BigDecimal penaltyDeposit = BigDecimal.ZERO;
        if (request.getCustomerId() == null && vehicleOpt.isPresent()) {
            Vehicle vehicle = vehicleOpt.get();
            if (vehicle.getViolationCount() != null && vehicle.getViolationCount() > 3
                    && vehicle.getRestrictedUntil() != null && vehicle.getRestrictedUntil().isAfter(Instant.now())) {

                // Xe vi phạm nặng -> Lấy số tiền phạt cấu hình từ SystemSetting
                penaltyDeposit = systemSettingServiceImpl.getDepositAmount(SystemSettingServiceImpl.DEFAULT_DEPOSIT_AMOUNT);

                //CHỐT CHẶN: Đề phòng Frontend bị bypass nút bấm khi Staff chưa thực sự thu tiền
                if (!request.isPenaltyDepositCollected()) {
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

        //khấu trừ cái chuỗi slot được chọn tại quầy
        if (request.getChosenSlotIds() != null) {
            for (Integer slotId : request.getChosenSlotIds()) {
                BookingSlot slot = bookingSlotRepository.findById(slotId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.SERVICE_SLOT_NOT_AVAILABLE));

                slot.setBookedCount(slot.getBookedCount() + 1);
                if (slot.getBookedCount() >= slot.getMaxCapacity()) {
                    slot.setStatus("FULL"); // Tự động khóa slot real-time nếu hết chỗ
                }
                bookingSlotRepository.save(slot);
            }
        }

        //LẬP LỊCH HẸN MỚI (BOOKING) VỚI TRẠNG THÁI CHECKED_IN MẶC ĐỊNH
        Booking newBooking = Booking.builder()
                .vehicle(finalVehicle)
                .customer(request.getCustomerId() != null ? customerRepository.findById(request.getCustomerId()).orElse(null) : null)
                .appointmentDate(LocalDate.now())
                .status("CHECKED_IN") // Mặc định chuyển thẳng sang CHECKED_IN theo luồng Walk-In tại quầy
                .bookingType("WALK_IN")
                .createdAt(Instant.now())
                .checkInAt(Instant.now())
                .isDepositPaid(creditFromOldBooking.compareTo(BigDecimal.ZERO) > 0)
                .build();
        Booking savedBooking = bookingRepository.save(newBooking);

        //TÍNH TOÁN CHI TIẾT DÒNG TIỀN VÀ PHÁT HÀNH HÓA ĐƠN TỔNG (BookingInvoice)

        //Tách biệt doanh thu dịch vụ chính và dịch vụ bổ sung (Addon) để lưu báo cáo
        BigDecimal serviceAmount = BigDecimal.ZERO;
        BigDecimal addonAmount = BigDecimal.ZERO;

        if (request.getServicePackageId() != null) {
            ServicePackage servicePackage = servicePackageRepository.findById(request.getServicePackageId()).get();
            serviceAmount = servicePackage.getBasePrice();
        }

        if (request.getAddonIds() != null && !request.getAddonIds().isEmpty()) {
            for (Integer addonId : request.getAddonIds()) {
                AddonService addon = addonServiceRepository.findById(addonId).get();
                addonAmount = addonAmount.add(addon.getPrice());
            }
        }

        //Tính toán tổng số tiền được giảm giá (discount_amount)
        // Tổng giảm giá ở bước Check-in = Tiền giảm trừ gói Unlimited + Tiền cọc phạt thu trước + Tiền cứu cọc trễ hẹn
        // (Bản chất penaltyDeposit và creditFromOld Booking thu trước đều làm giảm số tiền phải thu lúc Checkout)
        BigDecimal totalDiscountAmount = packageDiscount.add(penaltyDeposit).add(creditFromOldBooking);

        //Áp dụng công thức tính số tiền khách thực tế phải thu nốt khi lấy xe (final_amount)
        BigDecimal remainingBalanceAtCheckout = rawAmount.subtract(totalDiscountAmount);

        if (remainingBalanceAtCheckout.compareTo(BigDecimal.ZERO) < 0) {
            remainingBalanceAtCheckout = BigDecimal.ZERO;
        }

        //KHỞI TẠO ĐẦY ĐỦ THỰC THỂ BOOKING_INVOICE
        BookingInvoice invoice = BookingInvoice.builder()
                .booking(savedBooking) // Khóa ngoại duy nhất trỏ tới Booking (booking_id)
                .customer(savedBooking.getCustomer()) // Khóa ngoại khách hàng (customer_id), tự động lấy từ booking (có thể null nếu khách vãng lai mới)
                //Các trường dòng tiền tổng
                .rawAmount(rawAmount) // Tổng tiền gốc ban đầu (serviceAmount + addonAmount)
                .discountAmount(totalDiscountAmount) // Tổng số tiền được giảm trừ/khấu trừ
                .finalAmount(remainingBalanceAtCheckout) // Số tiền thực tế khách cần móc bóp trả lúc Checkout
                //Các trường bóc tách doanh thu dịch vụ cụ thể
                .serviceAmount(serviceAmount) // Tổng giá trị của riêng dịch vụ chính
                .addonAmount(addonAmount) // Tổng giá trị của các dịch vụ bổ sung đính kèm
                //Các trường giảm giá từ Loyalty/Khuyến mãi (Luồng Walk-in tại quầy mặc định khởi tạo = 0.00)
                .voucherDiscount(BigDecimal.ZERO)
                .pointDiscount(BigDecimal.ZERO)
                //Trạng thái và Thời gian
                .status("PENDING") // Trạng thái hóa đơn ban đầu: Chờ rửa xe xong khách ra trả tiền mới đổi sang PAID
                .paidAt(null) // Chưa thanh toán thành công lúc check-in nên để null
                .build();
        // Thực hiện lưu chính thức xuống bảng booking_invoice dưới DB
        bookingInvoiceRepository.save(invoice);


        //CẤP PHÁT SỐ VÉ THỨ TỰ HÀNG ĐỢI VẬT LÝ (QueueTicket)
        //Sinh số thứ tự vé tiếp theo dựa vào stationId và diện khách Walk-In (false)
        String nextTicketNumber = queueTicketService.generateTicketNumber(request.getStationId(), false);

        //gọi từ StationRepository để làm khóa ngoại station_id.

        Station currentStation = stationRepository.findById(request.getStationId())
                .orElseThrow(() -> new BusinessException(ErrorCode.STATION_NOT_FOUND));

        //KHỞI TẠO ĐẦY ĐỦ THỰC THỂ QUEUE_TICKET THEO DB
        QueueTicket ticket = QueueTicket.builder()
                .station(currentStation) //Vé thuộc chi nhánh nào (station_id - NOT NULL)
                .booking(savedBooking)   // Khóa ngoại trỏ sang lịch hẹn vừa tạo (booking_id - NULLABLE)
                .ticketNumber(nextTicketNumber) // Số thứ tự hiển thị (ticket_number - NOT NULL)

                //Trạng thái và Phân loại
                .status("CHECKED_IN")    // Trạng thái vé (status - NOT NULL)
                .isBooking(false)        // Đánh dấu KHÔNG PHẢI đơn đặt trước (is_booking - NOT NULL)
                .priorityScore(0)        // Điểm ưu tiên mặc định cho khách vãng lai (priority_score)
                //Trường 'issued_at' đã được cấu hình @CreationTimestamp trong Entity
                // nên khi lưu xuống DB, Hibernate sẽ tự động điền thời gian hiện tại, không cần set tay ở đây.
                .build();

        //Lưu chính thức xuống bảng queue_ticket dưới DB
        QueueTicket savedTicket = queueTicketRepository.save(ticket);


        //TRẢ VỀ ĐÚNG KHUÔN MẪU CreateWalkInResponse PHỤC VỤ FRONT-END IN VÉ
        return CreateWalkInResponse.builder()
                .bookingId(savedBooking.getId())
                .queueTicketId(ticket.getId())
                .ticketNumber(nextTicketNumber)
                .status(savedBooking.getStatus())
                .remainingBalance(remainingBalanceAtCheckout)
                .message("Walk-in booking has been created successfully. The vehicle has been added to the service queue.")
                .build();
    }


}
