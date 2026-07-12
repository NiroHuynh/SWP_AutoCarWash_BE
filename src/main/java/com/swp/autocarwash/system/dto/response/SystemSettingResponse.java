package com.swp.autocarwash.system.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemSettingResponse {
    private Integer id;

    @JsonProperty("setting_key")
    private String settingKey;

    @JsonProperty("setting_value")
    private String settingValue;

    private String category;

    @JsonProperty("data_type")
    private String dataType;

    private String description;
}
