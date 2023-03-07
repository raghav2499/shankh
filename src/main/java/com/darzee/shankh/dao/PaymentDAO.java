package com.darzee.shankh.dao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentDAO {
    private Long id;

    private Double orderAmount;

    private Double amountPaid;

    private OrderDAO order;

    public PaymentDAO(Double orderAmount, Double amountPaid, OrderDAO order) {
        this.orderAmount = orderAmount;
        this.amountPaid = amountPaid;
        this.order = order;
    }
}
