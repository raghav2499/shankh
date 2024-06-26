package com.darzee.shankh.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PriceBreakUpDetails {
    private Long id;
    private String component;
    private Double value;
    private Integer componentQuantity = 1;
    private Boolean isDeleted;
}
