package com.swp.autocarwash.customer.scheduler;

import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.entity.Vehicle;
import com.swp.autocarwash.customer.repository.CustomerRepository;
import com.swp.autocarwash.customer.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

/**
 * BR-01: reset chủ động violation_count/restricted_until khi án phạt đã hết hạn,
 * chạy độc lập với sự kiện hủy booking (bổ trợ cho reset-on-expiry lazy trong
 * BookingCanceledEventListener, vốn chỉ reset đúng 1 bảng mỗi lần và có thể trễ
 * rất lâu nếu không có sự kiện hủy tiếp theo cùng loại).
 *
 * Quét cả customer và vehicle mỗi ngày, reset đồng thời mọi dòng đã hết hạn.
 *
 * @author KimNgan
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ViolationResetScheduler {

    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;

    /**
     * Chạy vào 00:05:00 mỗi ngày (giờ server).
     */
    @Scheduled(cron = "*/10 * * * * ?") // TEMP-TEST: chạy mỗi 10s để verify, sẽ revert về "0 5 0 * * ?"
    //@Scheduled(cron = "0 5 0 * * ?")
    @Transactional
    public void resetExpiredRestrictions() {
        Instant now = Instant.now();

        List<Customer> expiredCustomers = customerRepository.findByRestrictedUntilBefore(now);
        for (Customer customer : expiredCustomers) {
            customer.setViolationCount(0);
            customer.setRestrictedUntil(null);
            customerRepository.save(customer);
        }

        List<Vehicle> expiredVehicles = vehicleRepository.findByRestrictedUntilBefore(now);
        for (Vehicle vehicle : expiredVehicles) {
            vehicle.setViolationCount(0);
            vehicle.setRestrictedUntil(null);
            vehicleRepository.save(vehicle);
        }

        log.info("[ViolationResetScheduler] Reset {} customer(s), {} vehicle(s) with expired restriction",
                expiredCustomers.size(), expiredVehicles.size());
    }
}
