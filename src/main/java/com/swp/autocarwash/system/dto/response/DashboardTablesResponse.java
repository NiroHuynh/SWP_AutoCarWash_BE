package com.swp.autocarwash.system.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardTablesResponse {

    private List<PackageStatResponse> packageStats;

    private List<TierStatResponse> tierStats;

}
