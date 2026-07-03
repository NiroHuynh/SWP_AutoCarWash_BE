package com.swp.autocarwash.loyalty.controller;

import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.loyalty.dto.response.LoyaltyOverviewResponse;
import com.swp.autocarwash.loyalty.dto.response.LoyaltyTransactionPageResponse;
import com.swp.autocarwash.loyalty.service.LoyaltyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/loyalty")
@RequiredArgsConstructor
public class LoyaltyController {

    private final LoyaltyService loyaltyService;

    @GetMapping("/overview")
    public ApiResponse<LoyaltyOverviewResponse> getOverview() {

        return ApiResponse.<LoyaltyOverviewResponse>builder()
                .success(true)
                .message("Get loyalty overview successfully")
                .data(loyaltyService.getOverview())
                .build();
    }

    @GetMapping("/transactions")
    public ApiResponse<LoyaltyTransactionPageResponse> getTransactions(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return ApiResponse.<LoyaltyTransactionPageResponse>builder()
                .success(true)
                .message("Get loyalty transactions successfully")
                .data(loyaltyService.getTransactions(pageable))
                .build();
    }

}
