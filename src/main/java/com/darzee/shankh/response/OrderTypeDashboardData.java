package com.darzee.shankh.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderTypeDashboardData {

    private String orderType;
    private Double amount;

    public OrderTypeDashboardData(String orderType, Double amount) {
        this.orderType = orderType;
        this.amount = amount;
    }
}
