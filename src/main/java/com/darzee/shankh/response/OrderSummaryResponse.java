package com.darzee.shankh.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderSummaryResponse {
    private OrderSummary orderSummary;
    private String message;
    public OrderSummaryResponse(OrderSummary orderSummary, String message) {
        this.orderSummary = orderSummary;
        this.message = message;
    }
}