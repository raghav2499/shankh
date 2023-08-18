package com.darzee.shankh.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DeviceInfoRequest {

    @NotNull(message = "tailor_id is mandatory to save device info")
    public Long tailorId;
    public String deviceToken;
    public String appVersion;
}
