package com.swp.autocarwash.promotion.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreatePromotionVoucherResponse {

    private Integer promotionId;
    private Long voucherId;
    private String voucherCode;
    private List<String> voucherCodes;

}
