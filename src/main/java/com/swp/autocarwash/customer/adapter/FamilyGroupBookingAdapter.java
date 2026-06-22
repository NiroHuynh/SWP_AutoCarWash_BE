package com.swp.autocarwash.customer.adapter;

import com.swp.autocarwash.booking.port.FamilyGroupPort;
import com.swp.autocarwash.customer.service.family.FamilyGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("pro")
@RequiredArgsConstructor
public class FamilyGroupBookingAdapter implements FamilyGroupPort {

    private final FamilyGroupService familyGroupService;

    @Override
    public Long getOwnerCustomerIdOfCustomerId(Long customerId) {
        return familyGroupService.getOwnerCustomerIdOfCustomerId(customerId);
    }
}
