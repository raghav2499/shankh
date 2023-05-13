package com.darzee.shankh.request.innerObjects;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateOrderAmountDetails {
    private Double totalOrderAmount;
    /** rename advanceOrderAmount to orderAmountRecieved**/
    private Double advanceOrderAmount;
    private Double dueBalance;
}
