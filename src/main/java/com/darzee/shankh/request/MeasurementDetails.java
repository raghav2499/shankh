package com.darzee.shankh.request;

import com.darzee.shankh.enums.MeasurementScale;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Validated
public class MeasurementDetails {
    @NotNull(message = "customer_id is mandatory for saving measurement")
    private Long customerId;
    @NotNull(message = "outfit_type is mandatory for saving measurement")
    private Integer outfitType;
    private MeasurementScale scale;
    private Measurements measurements;
}
