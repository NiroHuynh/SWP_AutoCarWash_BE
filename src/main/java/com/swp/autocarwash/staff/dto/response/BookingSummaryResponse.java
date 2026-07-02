package com.swp.autocarwash.staff.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingSummaryResponse {
    private BigDecimal rawAmount;
    private BigDecimal packageDiscount;
    private BigDecimal penaltyDeposit;
    private BigDecimal transferredCredit;
    private BigDecimal remainingBalance;
    private String systemNotice;
    private boolean isActionBlock; //cho fe nhận diện hiển thị/hide button thu cọc

    private List<AvailableSlotDTO> availableSlots;

    @Getter
    @Setter
    @Builder
    public static class AvailableSlotDTO {
        private Long slotId; // [cite: 159, 161]
        private LocalTime startTime; // [cite: 165]
        private LocalTime endTime; // [cite: 167]

        private List<Long> associatedSlotIds;
    }
}
//rawAmount , packageDiscount (Khấu trừ gói) ,
// penaltyDeposit (Cọc phạt 20k) , transferredCredit (Cọc cũ chuyển sang),
// và remainingBalance (Tổng tiền cần thu lúc lấy xe).
