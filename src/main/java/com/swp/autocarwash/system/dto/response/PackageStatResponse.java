package com.swp.autocarwash.system.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackageStatResponse {

    private String packageName;

    private Long bookingCount;

    private Integer percentage;

}
