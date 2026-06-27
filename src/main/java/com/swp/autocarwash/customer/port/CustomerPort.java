package com.swp.autocarwash.customer.port;

import com.swp.autocarwash.common.contract.customer.CustomerContract;
import com.swp.autocarwash.customer.entity.Customer;

public interface CustomerPort {

    /**
     * Get customer tương ứng với userId (lấy từ JWT của user đang đăng nhập).
     *
     * @param userId id của user
     * @return customer entity tương ứng
     */
    Customer getCustomerReferenceByUserId(
            Long userId
    );
}
