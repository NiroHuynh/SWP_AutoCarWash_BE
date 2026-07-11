package com.swp.autocarwash.loyalty.service;

import java.time.LocalDateTime;

/**
 * Danh gia lai hang thanh vien theo chu ky nam (BR-FE-44) va reset diem cho chu ky moi.
 *
 * @author KimNgan
 * @version 1.0
 */
public interface AnnualTierEvaluationService {

    /**
     * Danh gia hang cua 1 khach hang dua tren tong chi tieu trong khoang [yearStart, yearEnd),
     * cap nhat FK hang (co the UPGRADE hoac DOWNGRADE), ghi lich su neu hang thay doi, roi
     * reset totalPoints + accumulatedPoints ve 0 de bat dau chu ky tich luy moi.
     *
     * <p>Chay trong transaction rieng (REQUIRES_NEW) de 1 khach loi khong lam rollback
     * toan bo job.</p>
     */
    void evaluateAndResetOneCustomer(Long customerId, LocalDateTime yearStart, LocalDateTime yearEnd);
}
