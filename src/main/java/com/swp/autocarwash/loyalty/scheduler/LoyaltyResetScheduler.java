package com.swp.autocarwash.loyalty.scheduler;

import com.swp.autocarwash.loyalty.entity.LoyaltyPointBalance;
import com.swp.autocarwash.loyalty.repository.LoyaltyPointBalanceRepository;
import com.swp.autocarwash.loyalty.service.AnnualTierEvaluationService;
import com.swp.autocarwash.system.service.SystemSettingService;
import com.swp.autocarwash.system.service.impl.SystemSettingServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.time.ZoneId;

/**
 * Job danh gia lai hang thanh vien va reset diem loyalty thuong nien (BR-FE-44).
 *
 * <p>Chay 00:05 moi ngay; chi thuc su chay khi hom nay dung bang ngay cau hinh
 * {@code LOYALTY_RESET_MONTH_DAY} (dinh dang MM-DD). Voi moi khach hang: tinh tong
 * chi tieu nam vua qua, danh gia lai hang (UPGRADE/DOWNGRADE/giu nguyen), ghi lich su
 * neu hang thay doi, roi reset ca totalPoints va accumulatedPoints ve 0 de bat dau
 * chu ky tich luy moi. Logic per-customer nam trong {@link AnnualTierEvaluationService},
 * chay trong transaction rieng (REQUIRES_NEW) de 1 khach loi khong lam rollback ca job.</p>
 *
 * @author KimNgan
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class LoyaltyResetScheduler {

    private static final ZoneId ZONE = ZoneId.of("Asia/Ho_Chi_Minh");
    private static final int PAGE_SIZE = 200;

    private final LoyaltyPointBalanceRepository balanceRepository;
    private final AnnualTierEvaluationService annualTierEvaluationService;
    private final SystemSettingService systemSettingService;

    /** Chay luc 00:05:00 moi ngay theo gio Viet Nam. */
    @Scheduled(cron = "0 5 0 * * ?", zone = "Asia/Ho_Chi_Minh")
    // TODO: chi de test thu cong - GO BO truoc khi merge/deploy production.
    //@Scheduled(cron = "0/10 * * * * ?", zone = "Asia/Ho_Chi_Minh")
    public void resetLoyaltyPoints() {
        MonthDay configured = parseConfig(); // configured lấy từ database - bảng system_setting, key = LOYALTY_RESET_MONTH_DAY, value = "MM-DD"
        MonthDay todayMd = MonthDay.now(ZONE);
        if (!todayMd.equals(configured)) {
            return; // Hom nay khong phai ngay reset -> bo qua
        }

        LocalDate today = LocalDate.now(ZONE);
        int previousYear = today.getYear() - 1;
//        LocalDateTime yearStart = LocalDate.of(previousYear, 7, 7).atStartOfDay();
//        LocalDateTime yearEnd = LocalDate.of(previousYear + 1, 7, 7).atStartOfDay();
        LocalDateTime yearStart = LocalDate.of(previousYear, configured.getMonthValue(), configured.getDayOfMonth()).atStartOfDay();
        LocalDateTime yearEnd = LocalDate.of(previousYear + 1, configured.getMonthValue(), configured.getDayOfMonth()).atStartOfDay();
        int processed = 0;
        int failed = 0;
        Pageable pageable = PageRequest.of(0, PAGE_SIZE, Sort.by("id"));
        Page<LoyaltyPointBalance> page;
        do {
            page = balanceRepository.findAll(pageable);
            for (LoyaltyPointBalance balance : page.getContent()) {
                try {
                    annualTierEvaluationService.evaluateAndResetOneCustomer(balance.getId(), yearStart, yearEnd);
                    processed++;
                } catch (Exception e) {
                    failed++;
                    log.error("[LoyaltyResetScheduler] Annual evaluation failed for customerId={}", balance.getId(), e);
                }
            }
            pageable = pageable.next();
        } while (page.hasNext());

        log.info("[LoyaltyResetScheduler] Done. processed={} failed={} year={}", processed, failed, previousYear);
    }

    private MonthDay parseConfig() {
        String mmdd = systemSettingService.getStringValue(SystemSettingServiceImpl.LOYALTY_RESET_MONTH_DAY).trim();
        String[] parts = mmdd.split("-");
        return MonthDay.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }
}
