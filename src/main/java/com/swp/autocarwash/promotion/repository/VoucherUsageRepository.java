package com.swp.autocarwash.promotion.repository;

import com.swp.autocarwash.promotion.entity.VoucherUsage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * Repository for VoucherUsage entity
 *
 * @author Phong
 * @version 1.0
 */
public interface VoucherUsageRepository extends JpaRepository<VoucherUsage, Integer> {

    long countByVoucherIdAndCustomerId(Integer voucherId, Integer customerId);
}
