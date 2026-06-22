package com.swp.autocarwash.subscription.service.impl;

import com.swp.autocarwash.subscription.repository.FamilySubscriptionRepository;
import com.swp.autocarwash.subscription.service.FamilySubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class FamilySubscriptionServiceImpl implements FamilySubscriptionService {


    private final FamilySubscriptionRepository familySubscriptionRepository;


    @Override
    public Integer getActiveServicePackageId(Long vehicleId) {

        if (vehicleId == null) {
            return null;
        }
        Integer servicePackageId =  familySubscriptionRepository
                .findActiveServicePackageIdByVehicleId(vehicleId);

        return servicePackageId;
    }
}
