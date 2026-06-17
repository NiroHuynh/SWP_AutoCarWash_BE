package com.swp.autocarwash.booking.port;


import com.swp.autocarwash.common.contract.loyalty.CustomerTierContract;

public interface LoyaltyPort {

    /**
     * Lấy tier hiện tại của customer
     * Dùng để tính booking window (AC requirement)
     */
    CustomerTierContract getCustomerTier(Integer customerId);

}
