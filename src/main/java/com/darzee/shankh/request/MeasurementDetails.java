package com.darzee.shankh.request;

import com.darzee.shankh.enums.MeasurementScale;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MeasurementDetails {
    private MeasurementScale scale;
    private Measurements measurements;
}
