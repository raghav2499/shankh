package com.darzee.shankh.request.innerObjects;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderAmountDetails {
    @NotNull(message = "total_order_amount cannot be null")
    private Double totalOrderAmount;
    private Double advanceOrderAmount;
    private Double dueBalance;
}
