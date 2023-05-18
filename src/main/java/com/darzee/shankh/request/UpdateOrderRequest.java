package com.darzee.shankh.request;

import com.darzee.shankh.request.innerObjects.UpdateOrderAmountDetails;
import com.darzee.shankh.request.innerObjects.UpdateOrderDetails;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Validated
public class UpdateOrderRequest {
    @Valid
    private UpdateOrderDetails orderDetails;

    @Valid
    private UpdateOrderAmountDetails orderAmountDetails;
}