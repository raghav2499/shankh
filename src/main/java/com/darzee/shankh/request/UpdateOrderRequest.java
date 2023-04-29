package com.darzee.shankh.request;

import com.darzee.shankh.request.innerObjects.UpdateOrderAmountDetails;
import com.darzee.shankh.request.innerObjects.UpdateOrderDetails;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.Valid;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateOrderRequest {
    @Valid
    private UpdateOrderDetails orderDetails;

    @Valid
    private UpdateOrderAmountDetails orderAmountDetails;
}