package com.darzee.shankh.dao;

import com.darzee.shankh.request.PriceBreakUpDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PriceBreakupDAO {

    private Long id;
    private String component;
    private Double value;
    private Integer quantity;
    private OrderItemDAO orderItem;

    public PriceBreakupDAO(PriceBreakUpDetails priceBreakUpDetails, OrderItemDAO orderItem) {
        this.component = priceBreakUpDetails.getComponent();;
        this.value = priceBreakUpDetails.getValue();
        this.quantity = priceBreakUpDetails.getComponentQuantity();
        this.orderItem = orderItem;
    }

    public boolean isComponentStringUpdated(String value) {
        return value != null && !this.component.equals(value);
    }

    public boolean isValueUpdated(Double value) {
        return value != null && !this.value.equals(value);
    }
    public boolean isQuantityUpdated(Integer value) {
        return value != null && !this.quantity.equals(value);
    }



}
