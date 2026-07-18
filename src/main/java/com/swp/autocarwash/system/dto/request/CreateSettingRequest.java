package com.swp.autocarwash.system.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreateSettingRequest {

    @JsonProperty("setting_key")
    private String settingKey;

    @JsonProperty("setting_value")
    private String settingValue;

    private String category;

    @JsonProperty("data_type")
    private String dataType;

    private String description;
}
