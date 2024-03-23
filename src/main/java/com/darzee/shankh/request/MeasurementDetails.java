package com.darzee.shankh.request;

import com.darzee.shankh.enums.MeasurementScale;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Validated
@AllArgsConstructor
@NoArgsConstructor
public class MeasurementDetails {
    @NotNull(message = "customer_id is mandatory for saving measurement")
    private Long customerId;
    @NotNull(message = "outfit_type is mandatory for saving measurement")
    private Integer outfitType;
    private MeasurementScale scale;
    private Map<String, Double> measurements;
    private String referenceId;
}
