package com.swp.autocarwash.subscription.service.impl;

import com.swp.autocarwash.subscription.repository.UnlimitSubscriptionRepository;
import com.swp.autocarwash.subscription.service.UnlimitSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnlimitSubscriptionServiceImpl implements UnlimitSubscriptionService {

    private final UnlimitSubscriptionRepository unlimitSubscriptionRepository;

    @Override
    public Integer getActiveServicePackageId(Long vehicleId) {
        if(vehicleId==null){
            return null;
        }

        return unlimitSubscriptionRepository.findActiveServicePackageIdByVehicleId(vehicleId);
    }


}
