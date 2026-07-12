package com.swp.autocarwash.customer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupSubscriptionDto {

    private String planName;
    private String status;
    private LocalDate endDate;
    private String usageSummary; // Trả về dạng chuỗi chu kỳ "3/5" để FE làm Progress Bar
}
