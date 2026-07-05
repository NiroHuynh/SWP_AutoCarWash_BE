package com.swp.autocarwash.customer.event.listener;


import com.swp.autocarwash.booking.entity.enums.BookingType;
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
 *   <li>bookingType = SUBSCRIPTION (gói Unlimited/Family, không cọc) → AC03:
 *       cộng violationCount lên Customer.</li>
 *   <li>bookingType = ADVANCE, isDepositPaid = true → AC02: "thu 100% tiền cọc"
 *       (ghi nhận tịch thu ở BookingServiceImpl, không refund).</li>
 * </ul>
 *
 * <p><b>Cửa sổ 30 ngày</b> được xấp xỉ bằng cơ chế reset-on-expiry (BR-01): mỗi lần
 * áp vi phạm mới, nếu restricted_until đã hết hạn thì reset violationCount=0 +
 * restricted_until=null TRƯỚC khi +1. Nhờ vậy không cần bảng lịch sử vi phạm riêng.</p>
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
        if(BookingType.WALK_IN.name().equals(event.getBookingType())){
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
        //0. BR-01: nếu án phạt cũ đã hết hạn -> reset counter + penalty TRƯỚC khi cộng vi phạm mới
        if(isRestrictionExpired(customer.getRestrictedUntil())){
            customer.setViolationCount(0);
            customer.setRestrictedUntil(null);
        }
        //1. cộng điểm phạt lên 1 đơn vị
        int count = (customer.getViolationCount() == null ? 0 : customer.getViolationCount()) + 1;
        //2. save điểm phạt
        customer.setViolationCount(count);
        //3. Check điều kiện của điểm phạt (BR-02): chỉ phạt khi > 3
        if(count > VIOLATION_THRESHOULD){
            customer.setRestrictedUntil(Instant.now().plus(CUSTOMER_RESTRICTION_DAYS, ChronoUnit.DAYS));
        }
        customerRepository.save(customer);

    }

    /**
     * BR-01: án phạt được coi là hết hạn khi restricted_until != null và đã < NOW().
     * restricted_until = null (chưa từng bị phạt) không tính là hết hạn -> không reset.
     */
    private boolean isRestrictionExpired(Instant restrictedUntil) {
        return restrictedUntil != null && restrictedUntil.isBefore(Instant.now());
    }

    /**
     * Chức năng: cộng 1 điểm vi phạm cho vehicle; nếu vượt ngưỡng thì khóa xe.
     *
     * @param vehicleId id của vehicle liên quan đến booking bị hủy
     */
    private void applyVehicleViolation(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow();
        //0. BR-01: reset khi án phạt cũ (theo license_plate) đã hết hạn, trước khi cộng vi phạm mới
        if(isRestrictionExpired(vehicle.getRestrictedUntil())){
            vehicle.setViolationCount(0);
            vehicle.setRestrictedUntil(null);
        }
        int count = (vehicle.getViolationCount() == null ? 0 : vehicle.getViolationCount()) + 1;
        vehicle.setViolationCount(count);
        //BR-02: chỉ phạt khi > 3
        if(count > VIOLATION_THRESHOULD){
            vehicle.setRestrictedUntil(Instant.now().plus(VEHICLE_RESTRICTION_DAYS, ChronoUnit.DAYS)); //ChronoUnit.DAYS cộng theo đơn vị ngày
        }
        vehicleRepository.save(vehicle);
    }


}
