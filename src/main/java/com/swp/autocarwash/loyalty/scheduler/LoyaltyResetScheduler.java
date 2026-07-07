package com.swp.autocarwash.loyalty.scheduler;

import com.swp.autocarwash.loyalty.entity.LoyaltyPointBalance;
import com.swp.autocarwash.loyalty.entity.LoyaltyPointTransaction;
import com.swp.autocarwash.loyalty.entity.enums.LoyaltyPointTransactionType;
import com.swp.autocarwash.loyalty.repository.LoyaltyPointBalanceRepository;
import com.swp.autocarwash.loyalty.repository.LoyaltyPointTransactionRepository;
import com.swp.autocarwash.system.service.SystemSettingService;
import com.swp.autocarwash.system.service.impl.SystemSettingServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.MonthDay;
import java.time.ZoneId;
import java.util.List;

/**
 * Job reset diem loyalty thuong nien (BL-LY-12, BL-LY-13).
 *
 * <p>Chay 00:05 moi ngay; chi thuc su reset khi hom nay dung bang ngay cau hinh
 * {@code LOYALTY_RESET_MONTH_DAY} (dinh dang MM-DD). Khi reset: dua totalPoints ve 0
 * nhung GHI 1 dong LoyaltyPointTransaction loai RESET de bao toan lich su (audit),
 * va GIU NGUYEN accumulatedPoints (diem lifetime dung xet hang).</p>
 *
 * @author KimNgan
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class LoyaltyResetScheduler {

    private static final ZoneId ZONE = ZoneId.of("Asia/Ho_Chi_Minh");


    private final LoyaltyPointBalanceRepository balanceRepository;
    private final LoyaltyPointTransactionRepository transactionRepository;
    private final SystemSettingService systemSettingService;

    /** Chay luc 00:05:00 moi ngay theo gio Viet Nam. */
    @Scheduled(cron = "0 5 0 * * ?", zone = "Asia/Ho_Chi_Minh")
    /** Chạy 10s để test */
    //@Scheduled(cron = "0/10 * * * * ?", zone = "Asia/Ho_Chi_Minh")
    @Transactional
    public void resetLoyaltyPoints() {
        MonthDay configured = parseConfig();
        MonthDay todayMd = MonthDay.now(ZONE);
        if (!todayMd.equals(configured)) {
            return; // Hom nay khong phai ngay reset -> bo qua
        }

        List<LoyaltyPointBalance> balances = balanceRepository.findByTotalPointsGreaterThan(0);
        if (balances.isEmpty()) {
            log.info("[LoyaltyResetScheduler] No balance to reset for {}", todayMd);
            return;
        }

        int resetCount = 0;
        for (LoyaltyPointBalance balance : balances) {
            int current = balance.getTotalPoints() == null ? 0 : balance.getTotalPoints();

            // Ghi dong RESET de bao toan audit (BL-LY-13)
            LoyaltyPointTransaction txn = new LoyaltyPointTransaction();
            txn.setCustomer(balance.getCustomer());
            txn.setBooking(null);
            txn.setTransactionType(LoyaltyPointTransactionType.RESET);
            txn.setPoints(-current);
            txn.setBalanceAfter(0);
            transactionRepository.save(txn);

            // Dua so du kha dung ve 0, giu nguyen accumulatedPoints
            balance.setTotalPoints(0);
            balanceRepository.save(balance);
            resetCount++;
        }
        log.info("[LoyaltyResetScheduler] Done. Reset points for {} customers on {}", resetCount, todayMd);
    }

    private MonthDay parseConfig() {
        String mmdd = systemSettingService.getStringValue(SystemSettingServiceImpl.LOYALTY_RESET_MONTH_DAY).trim();
        String[] parts = mmdd.split("-");
        return MonthDay.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }
}
