package com.swp.autocarwash.promotion.repository;

import com.swp.autocarwash.promotion.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 *
 * Repository for Voucher entity
 *
 * @author Phong
 * @version 1.0
 */
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {

    Optional<Voucher> findByVoucherCode(String code);

    boolean existsByVoucherCode(String code);
}