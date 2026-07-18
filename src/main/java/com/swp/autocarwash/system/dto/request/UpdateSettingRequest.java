package com.swp.autocarwash.system.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateSettingRequest {

    @JsonProperty("setting_value")
    private String settingValue;

}
