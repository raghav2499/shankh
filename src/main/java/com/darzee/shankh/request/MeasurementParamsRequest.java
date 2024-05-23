package com.darzee.shankh.request;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@Validated
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MeasurementParamsRequest {
    @NotNull(message = "Outfit type is mandatory")
    @Min(value = 0,message = "Invalid outfit type")
    @Max(value=15,message = "Invalid outfit type")
    private Integer outfitType;

    @NotNull(message = "Outfit side is mandatory")
    @Min(value = 0,message = "Invalid outfit side")
    @Max(2)
    private Integer outfitSide;

    @NotEmpty(message = "Request is mandatory")
    private List<Map<String, Object>> measurementPramList;
}
