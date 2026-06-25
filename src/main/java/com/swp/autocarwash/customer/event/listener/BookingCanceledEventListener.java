package com.swp.autocarwash.customer.event.listener;


import com.swp.autocarwash.booking.event.BookingCanceledEvent;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.entity.Vehicle;
import com.swp.autocarwash.customer.repository.CustomerRepository;
import com.swp.autocarwash.customer.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Chức năng: Lắng nghe BookingCanceledEvent để áp dụng hình phạt cho xe

 * <ul>
 *   <li>bookingType = WALK_IN → AC04: cộng violationCount lên Vehicle.</li>
 *   <li>bookingType = ONLINE, isDepositPaid = false (gói Unlimited/Family) → AC03:
 *       cộng violationCount lên Customer.</li>
 *   <li>bookingType = ONLINE, isDepositPaid = true → AC02: "thu 100% tiền cọc".</li>
 * </ul>
 *
 * <p><b>Giới hạn đã biết</b>: violationCount là bộ đếm cộng dồn vĩnh viễn, chưa
 * tách riêng theo cửa sổ 30 ngày như AC yêu cầu chính xác — muốn đúng hoàn toàn
 * cần thêm 1 bảng lịch sử vi phạm có timestamp riêng.</p>
 *
 * @author KimNgan
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class BookingCanceledEventListener {
   private static final int VIOLATION_THRESHOULD = 3;
    private static final int CUSTOMER_RESTRICTION_DAYS = 14;
    private static final int VEHICLE_RESTRICTION_DAYS = 14;

    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;

    /**
     * Gọi chức năng xử lí điểm khi BookingCanceledEvent bắn/pulish sự kiện: hiểu như là séttatus cancel thành công trong database, nếu cancel không thành công -> event ko được publish -> listener ko nghe thấy -> ko hành động gì hết
     * @param event dữ liệu từ sự kiện huỷ booking
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT) // Chỉ chạy sau khi booking cancel thành công — đảm bảo booking thật sự bị hủy rồi mới ghi violation
    @Transactional(propagation = Propagation.REQUIRES_NEW) // bắt buộc REQUIRES_NEW (Spring yêu cầu) — transaction gốc đã commit xong, listener cần tự mở transaction mới riêng để save() thực sự flush/commit xuống DB
    public void onBookingCanceled(BookingCanceledEvent event) {
        if("WALK_IN".equals(event.getBookingType())){
            applyVehicleViolation(event.getVehicleId());
        }else if(Boolean.FALSE.equals(event.getIsDepositPaid())){
            applyCustomerViolation(event.getCustomerId()); // dành cho khách có đăng ký gói unlimited/family
        }
    }

    /**
     * Chức năng: AC03 — cộng 1 điểm vi phạm cho customer; nếu vượt ngưỡng thì khóa
     * quyền đặt lịch trước 14 ngày.
     *
     * @param customerId id của customer liên quan đến booking bị hủy
     */
    private void applyCustomerViolation(Long customerId) {
        if(customerId == null) return;
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        //1. cộng điểm phạt lên 1 đơn vị
        int count = (customer.getViolationCount() == null ? 0 : customer.getViolationCount()) + 1;
        //2. save điểm phạt
        customer.setViolationCount(count);
        //3. Check điều kiện của điểm phạt
        if(count > VIOLATION_THRESHOULD){
            customer.setRestrictedUntil(Instant.now().plus(VEHICLE_RESTRICTION_DAYS, ChronoUnit.DAYS));
        }
        customerRepository.save(customer);

    }

    /**
     * Chức năng: cộng 1 điểm vi phạm cho vehicle; nếu vượt ngưỡng thì khóa xe.
     *
     * @param vehicleId id của vehicle liên quan đến booking bị hủy
     */
    private void applyVehicleViolation(Integer vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow();
        int count = (vehicle.getViolationCount() == null ? 0 : vehicle.getViolationCount()) + 1;
        vehicle.setViolationCount(count);
        if(count > VIOLATION_THRESHOULD){
            vehicle.setRestrictedUntil(Instant.now().plus(VEHICLE_RESTRICTION_DAYS, ChronoUnit.DAYS)); //ChronoUnit.DAYS cộng theo đơn vị ngày
        }
        vehicleRepository.save(vehicle);
    }


}
