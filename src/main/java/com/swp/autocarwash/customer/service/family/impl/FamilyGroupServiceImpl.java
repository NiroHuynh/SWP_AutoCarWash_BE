package com.swp.autocarwash.customer.service.family.impl;

import com.swp.autocarwash.customer.repository.FamilyGroupRepository;
import com.swp.autocarwash.customer.service.family.FamilyGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FamilyGroupServiceImpl implements FamilyGroupService {

    private final FamilyGroupRepository familyGroupRepository;

    @Override
    public Long getOwnerCustomerIdOfCustomerId(Long customerId) {
        if(customerId==null){
            return null;
        }
        return familyGroupRepository.findOwnerCustomerIdByMemberCustomerId(customerId);
    }
}
