package com.swp.autocarwash.wash.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateWashLaneRequest {

    @NotNull(message = "Station Id can not be null")
    private Integer stationId;
    @NotBlank(message = "Lane name must not be blank")
    private String laneName;
    @NotBlank(message = "Status must not be blank")
    private String status;
    @NotNull(message = "Booking flow priority must not be null")
    @Min(value = 1, message = "Số lượt xe đặt trước ưu tiện phải là một số nguyên dương lớn hơn 0 (Tối thiểu là 1)")
    private Integer bookingWalkinRatio;

    public void setLaneName(String laneName){
        this.laneName = (laneName !=null) ? laneName.trim() : null;
    }
}
