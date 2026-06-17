package com.swp.autocarwash.servicepackage.adapter.mock;

import com.swp.autocarwash.booking.port.ServicePackagePort;
import com.swp.autocarwash.common.contract.servicepackage.ServicePackageContract;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class MockServicePackageBookingAdapter implements ServicePackagePort {

    /**
     * Lấy toàn bộ service package (mock data)
     * Không filter theo station theo yêu cầu AC
     */
    @Override
    public List<ServicePackageContract> getAllPackages() {
        return List.of(
                new ServicePackageContract(1, "Basic Wash", new BigDecimal("50000"), 15),
                new ServicePackageContract(2, "Premium Wash", new BigDecimal("90000"), 25),
                new ServicePackageContract(3, "Full Detailing", new BigDecimal("150000"), 45)
        );
    }

    @Override
    public Integer getDuration(Integer servicePackageId) {
        return 15;
    }
}
