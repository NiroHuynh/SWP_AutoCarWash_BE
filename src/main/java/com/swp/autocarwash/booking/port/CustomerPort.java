package com.swp.autocarwash.booking.port;


import com.swp.autocarwash.common.contract.customer.CustomerContract;

public interface CustomerPort {

    /**
     * Lấy thông tin customer theo userId hiện tại
     * Dùng cho mọi flow booking (context, create booking, validate,...)
     */
    CustomerContract getCustomerById(Integer customerId);

    /**
     * Kiểm tra customer có hợp lệ để đặt lịch hay không
     * (active, not blocked, not restricted,...)
     */
    boolean isEligibleForBooking(Integer customerId);
}
