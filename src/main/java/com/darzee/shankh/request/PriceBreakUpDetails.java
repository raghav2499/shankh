package com.darzee.shankh.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PriceBreakUpDetails {
    @NotNull(message = "component in price_breakup cannot be null")
    private String component;
    @NotNull(message = "value in price_breakup cannot be null")
    private Double value;
    private Integer componentQuantity = 1;
}
