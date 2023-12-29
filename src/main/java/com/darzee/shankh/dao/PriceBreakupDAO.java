package com.darzee.shankh.dao;

import com.darzee.shankh.request.PriceBreakUpDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PriceBreakupDAO {
    private String component;
    private Double value;
    private Integer quantity;
    private OrderItemDAO orderItem;

    public PriceBreakupDAO(PriceBreakUpDetails priceBreakUpDetails, OrderItemDAO orderItem) {
        this.component = priceBreakUpDetails.getComponent();;
        this.value = priceBreakUpDetails.getValue();
        this.quantity = priceBreakUpDetails.getQuantity();
        this.orderItem = orderItem;
    }
}
